package moddedmite.xylose.bettergamesetting.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;
import paulscode.sound.Channel;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class OpenALOutputLibrary extends LibraryLWJGLOpenAL {
	private static volatile String requestedDevice = "";

	public OpenALOutputLibrary() throws SoundSystemException {
		super();
	}

	public static void setRequestedDevice(String device) {
		requestedDevice = device == null ? "" : device;
	}

	public static String getRequestedDevice() {
		return requestedDevice;
	}

	public static final class AudioDevice {
		private final String key;
		private final String display;

		private AudioDevice(byte[] raw, int offset, int length) {
			byte[] raw1 = new byte[length];
			System.arraycopy(raw, offset, raw1, 0, length);
			this.key = Base64.getEncoder().encodeToString(raw1);
			this.display = decodeDisplay(raw1);
		}

		public String getKey() {
			return this.key;
		}

		public String getDisplay() {
			return this.display;
		}

		@Override
		public boolean equals(Object other) {
			return other instanceof AudioDevice && this.key.equals(((AudioDevice) other).key);
		}

		@Override
		public int hashCode() {
			return this.key.hashCode();
		}
	}

	public static List<AudioDevice> parseToken(int token) {
		List<AudioDevice> parsed = new ArrayList<>();
		try {
			parsed = parseDevices(readNalString(token));
		} catch (Throwable t) {
			BGSClient.logger.warn("BGS-ALC-RAW token=0x{} EXCEPTION {}: {}", Integer.toHexString(token), t.getClass().getSimpleName(), t.getMessage());
		}
		Map<String, AudioDevice> distinct = new LinkedHashMap<>();
		addAll(distinct, parsed);
		return Lists.newArrayList(distinct.values());
	}

	private static void addAll(Map<String, AudioDevice> distinct, List<AudioDevice> devices) {
		for (AudioDevice device : devices) {
			distinct.putIfAbsent(device.getKey(), device);
		}
	}

	private static List<AudioDevice> parseDevices(byte[] data) {
		List<AudioDevice> devices = Lists.newArrayList();
		int from = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0) {
				continue;
			}
			if (i > from) {
				devices.add(new AudioDevice(data, from, i - from));
			}
			from = i + 1;
			if (i + 1 < data.length && data[i + 1] == 0) {
				break;
			}
		}
		return devices;
	}

	public static String decodeKeyDisplay(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		}
		try {
			return decodeDisplay(Base64.getDecoder().decode(key));
		} catch (IllegalArgumentException e) {
			return key;
		}
	}

	private static String decodeDisplay(byte[] raw) {
		String decoded = new String(raw, StandardCharsets.UTF_8);
		if (decoded.indexOf('\uFFFD') >= 0) {
			decoded = new String(raw, Charset.forName("GB18030"));
		}
		return decoded;
	}

	private static byte[] readNalString(int token) {
		try {
			Method method = ALC10.class.getDeclaredMethod("nalcGetString", long.class, int.class);
			method.setAccessible(true);
			ByteBuffer buffer = (ByteBuffer) method.invoke(null, 0L, token);
			byte[] bytes = new byte[buffer.remaining()];
			buffer.duplicate().get(bytes);
			return bytes;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("ALC string enumeration failed", e);
		}
	}

	private static byte[] rawForKey(String key) {
		if (key == null || key.isEmpty()) {
			return null;
		}
		try {
			return Base64.getDecoder().decode(key);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public void init() throws SoundSystemException {
		boolean errors;
		byte[] requested = rawForKey(requestedDevice);
		boolean opened = false;
		if (requested != null) {
			try {
				if (AL.isCreated()) {
					AL.destroy();
				}
				AL.create(null, 44100, 60, false, false);
				opened = openDeviceByRawBytes(requested);
			} catch (Throwable t) {
				importantMessage("Opening audio device '" + decodeKeyDisplay(requestedDevice) + "' threw: " + t.getClass().getSimpleName() + ": " + t.getMessage());
			}
			if (!opened) {
				importantMessage("Failed to open audio device '" + decodeKeyDisplay(requestedDevice) + "', falling back to the system default.");
				try {
					if (AL.isCreated()) {
						AL.destroy();
					}
				} catch (Throwable ignored) {
				}
			}
		}
		if (!opened) {
			try {
				if (!AL.isCreated()) {
					AL.create();
				}
			} catch (LWJGLException e) {
				errorMessage("Unable to initialize OpenAL.  Probable cause: OpenAL not supported.");
				printStackTrace(e);
				throw new LibraryLWJGLOpenAL.Exception(e.getMessage(), LibraryLWJGLOpenAL.Exception.CREATE);
			}
		}
		errors = checkALError();

		if (errors) {
			importantMessage("OpenAL did not initialize properly!");
		} else {
			message("OpenAL initialized.");
		}

		FloatBuffer position = BufferUtils.createFloatBuffer(3).put(new float[]{listener.position.x, listener.position.y, listener.position.z});
		FloatBuffer orientation = BufferUtils.createFloatBuffer(6).put(new float[]{listener.lookAt.x, listener.lookAt.y, listener.lookAt.z, listener.up.x, listener.up.y, listener.up.z});
		FloatBuffer velocity = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		position.flip();
		orientation.flip();
		velocity.flip();

		setPrivateField("listenerPositionAL", position);
		setPrivateField("listenerOrientation", orientation);
		setPrivateField("listenerVelocity", velocity);

		AL10.alListener(AL10.AL_POSITION, position);
		errors = checkALError() || errors;
		AL10.alListener(AL10.AL_ORIENTATION, orientation);
		errors = checkALError() || errors;
		AL10.alListener(AL10.AL_VELOCITY, velocity);
		errors = checkALError() || errors;

		AL10.alDopplerFactor(SoundSystemConfig.getDopplerFactor());
		errors = checkALError() || errors;

		AL10.alDopplerVelocity(SoundSystemConfig.getDopplerVelocity());
		errors = checkALError() || errors;

		if (errors) {
			importantMessage("OpenAL did not initialize properly!");
			throw new LibraryLWJGLOpenAL.Exception("Problem encountered while loading OpenAL or creating the listener.  Probable cause:  OpenAL not supported", LibraryLWJGLOpenAL.Exception.CREATE);
		}

		Channel channel;
		for (int x = 0; x < SoundSystemConfig.getNumberStreamingChannels(); x++) {
			channel = createChannel(SoundSystemConfig.TYPE_STREAMING);
			if (channel == null) {
				break;
			}
			streamingChannels.add(channel);
		}
		for (int x = 0; x < SoundSystemConfig.getNumberNormalChannels(); x++) {
			channel = createChannel(SoundSystemConfig.TYPE_NORMAL);
			if (channel == null) {
				break;
			}
			normalChannels.add(channel);
		}
	}

	private boolean openDeviceByRawBytes(byte[] raw) {
		long deviceAddress = 0L;
		long contextAddress = 0L;
		try {
			ByteBuffer nameBuffer = BufferUtils.createByteBuffer(raw.length + 1);
			nameBuffer.put(raw).put((byte) 0).flip();
			deviceAddress = invokeNativeLong("nalcOpenDevice", MemoryUtil.getAddress(nameBuffer));
			if (deviceAddress == 0L) {
				errorMessage("OpenAL could not open the requested audio device.");
				teardownPartial(deviceAddress, contextAddress);
				return false;
			}

			contextAddress = invokeNativeLong("nalcCreateContext", deviceAddress, 0L);
			if (contextAddress == 0L) {
				errorMessage("OpenAL could not create a context on the requested audio device.");
				teardownPartial(deviceAddress, contextAddress);
				return false;
			}

			int madeCurrent = (Integer) nativeCall("nalcMakeContextCurrent", contextAddress);
			if (madeCurrent == 0) {
				errorMessage("OpenAL could not make the requested device context current.");
				teardownPartial(deviceAddress, contextAddress);
				return false;
			}

			ALCdevice device = newALCdevice(deviceAddress);
			ALCcontext context = newALCcontext(contextAddress);
			try {
				Method addContext = ALCdevice.class.getDeclaredMethod("addContext", ALCcontext.class);
				addContext.setAccessible(true);
				addContext.invoke(device, context);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException("Could not attach context to device", e);
			}
			@SuppressWarnings("unchecked")
			Map<Long, ALCdevice> devices = (Map<Long, ALCdevice>) field(ALC10.class, "devices").get(null);
			@SuppressWarnings("unchecked")
			Map<Long, ALCcontext> contexts = (Map<Long, ALCcontext>) field(ALC10.class, "contexts").get(null);
			synchronized (devices) {
				devices.put(deviceAddress, device);
				contexts.put(contextAddress, context);
			}
			field(AL.class, "device").set(null, device);
			field(AL.class, "context").set(null, context);
			field(AL.class, "created").setBoolean(null, true);
			message("Opened audio device '" + decodeKeyDisplay(requestedDevice) + "'.");
			return true;
		} catch (Throwable t) {
			errorMessage("Opening the requested audio device failed: " + t.getClass().getSimpleName() + ": " + t.getMessage());
			teardownPartial(deviceAddress, contextAddress);
			return false;
		}
	}

	private void teardownPartial(long deviceAddress, long contextAddress) {
		try {
			if (contextAddress != 0L) {
				invokeNativeLong("nalcMakeContextCurrent", 0L);
				invokeNativeLong("nalcDestroyContext", contextAddress);
			}
		} catch (Throwable ignored) {
		}
		try {
			if (deviceAddress != 0L) {
				invokeNativeLong("nalcCloseDevice", deviceAddress);
			}
		} catch (Throwable ignored) {
		}
	}

	private static long invokeNativeLong(String name, long... args) {
		Object result = nativeCall(name, args);
		return result == null ? 0L : (Long) result;
	}

	private static Object nativeCall(String name, long... args) {
		try {
			Class<?>[] types = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				types[i] = long.class;
			}
			Method method = ALC10.class.getDeclaredMethod(name, types);
			method.setAccessible(true);
			Object[] boxed = new Object[args.length];
			for (int i = 0; i < args.length; i++) {
				boxed[i] = args[i];
			}
			return method.invoke(null, boxed);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("ALC native call '" + name + "' failed", e);
		}
	}

	private static ALCdevice newALCdevice(long address) {
		try {
			Constructor<ALCdevice> ctor = ALCdevice.class.getDeclaredConstructor(long.class);
			ctor.setAccessible(true);
			return ctor.newInstance(address);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not wrap ALC device handle", e);
		}
	}

	private static ALCcontext newALCcontext(long address) {
		try {
			Constructor<ALCcontext> ctor = ALCcontext.class.getDeclaredConstructor(long.class);
			ctor.setAccessible(true);
			return ctor.newInstance(address);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not wrap ALC context handle", e);
		}
	}

	private static Field field(Class<?> clazz, String name) {
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("No such field " + clazz.getSimpleName() + "." + name, e);
		}
	}

	private void setPrivateField(String name, FloatBuffer value) {
		try {
			Field field = LibraryLWJGLOpenAL.class.getDeclaredField(name);
			field.setAccessible(true);
			field.set(this, value);
		} catch (ReflectiveOperationException e) {
			errorMessage("Could not initialize listener buffer '" + name + "': " + e.getMessage());
		}
	}

	private boolean checkALError() {
		return switch (AL10.alGetError()) {
			case AL10.AL_NO_ERROR -> false;
			case AL10.AL_INVALID_NAME -> {
				errorMessage("Invalid name parameter.");
				yield true;
			}
			case AL10.AL_INVALID_ENUM -> {
				errorMessage("Invalid parameter.");
				yield true;
			}
			case AL10.AL_INVALID_VALUE -> {
				errorMessage("Invalid enumerated parameter value.");
				yield true;
			}
			case AL10.AL_INVALID_OPERATION -> {
				errorMessage("Illegal call.");
				yield true;
			}
			case AL10.AL_OUT_OF_MEMORY -> {
				errorMessage("Unable to allocate memory.");
				yield true;
			}
			default -> {
				errorMessage("An unrecognized error occurred.");
				yield true;
			}
		};
	}
}
