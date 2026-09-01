package moddedmite.xylose.bettergamesetting.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;

import moddedmite.xylose.bettergamesetting.api.ISoundManager;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.*;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

public class SoundHandler implements ResourceManagerReloadListener, IUpdatePlayerListBox {
	private static final Gson field_147699_c = (new GsonBuilder()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
	private static final ParameterizedType field_147696_d = new ParameterizedType() {
		
		public Type[] getActualTypeArguments() {
			return new Type[]{String.class, SoundList.class};
		}
		
		public Type getRawType() {
			return Map.class;
		}
		
		public Type getOwnerType() {
			return null;
		}
	};
	public static final SoundPoolEntry MISSING_SOUND = new SoundPoolEntry("missing_sound", (URL) null);
	private final SoundRegistry sndRegistry = new SoundRegistry();
	private final SoundManager sndManager;
	private final ResourceManager mcResourceManager;
	
	public SoundHandler(ResourceManager resourceManager, SoundManager sndManager) {
		this.mcResourceManager = resourceManager;
		this.sndManager = sndManager;
	}

	public static SoundHandler formClient(Minecraft client) {
		return new SoundHandler(client.getResourceManager(), client.sndManager);
	}

	public void onResourceManagerReload(ResourceManager manager) {
		this.sndRegistry.clear();
		
		for (String s : (Set<String>) manager.getResourceDomains()) {
			try {
				List<Resource> list = manager.getAllResources(new ResourceLocation(s, "sounds.json"));
				for (Resource iresource : list.stream().filter(Objects::nonNull).toList()) {
					try {
						Map<String, SoundList> map = field_147699_c.fromJson(new InputStreamReader(iresource.getInputStream()), field_147696_d);
						for (Entry<String, SoundList> entry : map.entrySet()) {
							this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
						}
					} catch (RuntimeException runtimeexception) {
						BGSClient.logger.warn("Invalid sounds.json", runtimeexception);
					}
				}
			} catch (Exception exception) {
				if (exception instanceof FileNotFoundException) {
					BGSClient.logger.debug("Skipped missing sounds.json for domain {}", new Object[]{s});
				} else {
					BGSClient.logger.warn("Could not load sounds.json for domain {}", new Object[]{s}, exception);
				}
			}
		}
	}
	
	private void loadSoundResource(ResourceLocation p_147693_1_, SoundList p_147693_2_) {
		SoundEventAccessorComposite soundeventaccessorcomposite;
		
		if (this.sndRegistry.containsKey(p_147693_1_) && !p_147693_2_.canReplaceExisting()) {
			soundeventaccessorcomposite = (SoundEventAccessorComposite) this.sndRegistry.getObject(p_147693_1_);
		} else {
			BGSClient.logger.debug("Registered/replaced new sound event location {}", new Object[]{p_147693_1_});
			soundeventaccessorcomposite = new SoundEventAccessorComposite(p_147693_1_, 1.0D, 1.0D, p_147693_2_.getSoundCategory());
			this.sndRegistry.registerSound(soundeventaccessorcomposite);
		}
		
		for (Object o : p_147693_2_.getSoundList()) {
			final SoundList.SoundEntry soundentry = (SoundList.SoundEntry) o;
			String s = soundentry.getSoundEntryName();
			ResourceLocation resourcelocation1 = new ResourceLocation(s);
			final String s1 = s.contains(":") ? resourcelocation1.getResourceDomain() : p_147693_1_.getResourceDomain();
			Object object;
			
			switch (SwitchType.field_148765_a[soundentry.getSoundEntryType().ordinal()]) {
				case 1:
					ResourceLocation resourcelocation2 = new ResourceLocation(s1, "sounds/" + resourcelocation1.getResourcePath() + ".ogg");
					
//					try {
						this.mcResourceManager.getResource(resourcelocation2);
//					} catch (FileNotFoundException filenotfoundexception) {
//						logger.warn("File {} does not exist, cannot add it to event {}", new Object[]{resourcelocation2, p_147693_1_});
//						continue;
//					} catch (IOException ioexception) {
//						logger.warn("Could not load sound file " + resourcelocation2 + ", cannot add it to event " + p_147693_1_, ioexception);
//						continue;
//					}
					
					SoundPoolEntry soundpoolentry = new SoundPoolEntry(resourcelocation2.toString(), this.getSoundURL(resourcelocation2));
					soundpoolentry.setPitch(soundentry.getSoundEntryPitch());
					soundpoolentry.setVolume(soundentry.getSoundEntryVolume());
					soundpoolentry.setStreaming(soundentry.isStreaming());
					object = new SoundEventAccessor(soundpoolentry, soundentry.getSoundEntryWeight());
					break;
				case 2:
					object = new ISoundEventAccessor() {
						final ResourceLocation field_148726_a = new ResourceLocation(s1, soundentry.getSoundEntryName());
						
						public int func_148721_a() {
							SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
							return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.func_148721_a();
						}
						
						public SoundPoolEntry func_148720_g() {
							SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
							return soundeventaccessorcomposite1 == null ? SoundHandler.MISSING_SOUND : soundeventaccessorcomposite1.func_148720_g();
						}
					};
					break;
				default:
					throw new IllegalStateException("IN YOU FACE");
			}
			
			soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor) object);
		}
	}
	
	public SoundEventAccessorComposite getSound(ResourceLocation p_147680_1_) {
		return (SoundEventAccessorComposite) this.sndRegistry.getObject(p_147680_1_);
	}

	private URL getSoundURL(ResourceLocation location) {
		try {
			return new URL(null, "mcsounddomain:" + location.toString(), new SoundURLStreamHandler(this.mcResourceManager, location));
		} catch (MalformedURLException malformedurlexception) {
			return null;
		}
	}

	private static class SoundURLStreamHandler extends URLStreamHandler {
		private final ResourceManager resourceManager;
		private final ResourceLocation location;

		SoundURLStreamHandler(ResourceManager resourceManager, ResourceLocation location) {
			this.resourceManager = resourceManager;
			this.location = location;
		}

		protected URLConnection openConnection(URL url) {
			return new URLConnection(url) {
				public void connect() {
				}

				public InputStream getInputStream() throws IOException {
					return SoundURLStreamHandler.this.resourceManager.getResource(SoundURLStreamHandler.this.location).getInputStream();
				}
			};
		}
	}

	@Nullable
	public SoundEventAccessor getAccessor(ResourceLocation location) {
		return (SoundEventAccessor)this.sndRegistry.getObject(location);
	}
	
	/**
	 * Play a sound
	 */
	public void playSound(ISound sound) {
		((ISoundManager) this.sndManager).playSound(sound);
	}

	/**
	 * Plays the sound in n ticks
	 */
	public void playDelayedSound(ISound sound, int delay) {
		((ISoundManager) this.sndManager).addDelayedSound(sound, delay);
	}
	
	public void setListener(EntityPlayer player, float p_147691_2_) {
		this.sndManager.setListener(player, p_147691_2_);
	}
	
	public void pauseSounds() {
		this.sndManager.pauseAllSounds();
	}
	
	public void stopSounds() {
		this.sndManager.stopAllSounds();
	}
	
	public void unloadSounds() {
		((ISoundManager) this.sndManager).unloadSoundSystem();
	}

	/**
	 * Updates the JList with a new model.
	 */
	public void update() {
		((ISoundManager) this.sndManager).updateAllSounds();
	}
	
	public void resumeSounds() {
		this.sndManager.resumeAllSounds();
	}
	
	public void setSoundLevel(SoundCategory category, float volume) {
		if (category == SoundCategory.MASTER && volume <= 0.0F) {
			this.stopSounds();
		}

		((ISoundManager) this.sndManager).setSoundCategoryVolume(category, volume);
	}

	public void stopSound(ISound p_147683_1_) {
		((ISoundManager) this.sndManager).stopSound(p_147683_1_);
	}
	
	/**
	 * Returns a random sound from one or more categories
	 */
	public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... p_147686_1_) {
		ArrayList arraylist = Lists.newArrayList();
		
		for (Object o : this.sndRegistry.getKeys()) {
			ResourceLocation resourcelocation = (ResourceLocation) o;
			SoundEventAccessorComposite soundeventaccessorcomposite = (SoundEventAccessorComposite) this.sndRegistry.getObject(resourcelocation);
			
			if (ArrayUtils.contains(p_147686_1_, soundeventaccessorcomposite.getSoundCategory())) {
				arraylist.add(soundeventaccessorcomposite);
			}
		}
		
		if (arraylist.isEmpty()) {
			return null;
		} else {
			return (SoundEventAccessorComposite) arraylist.get((new Random()).nextInt(arraylist.size()));
		}
	}
	
	public boolean isSoundPlaying(ISound sound) {
		return ((ISoundManager) this.sndManager).isSoundPlaying(sound);
	}

	static final class SwitchType {
		static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];
		
		static {
			try {
				field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
			} catch (NoSuchFieldError ignored) {
			}
			
			try {
				field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
			} catch (NoSuchFieldError ignored) {
			}
		}
	}
}