package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.Minecraft;
import net.minecraft.Resource;
import net.minecraft.ResourceLocation;
import org.apache.commons.io.IOUtils;

/**
 * SoundPoolEntry
 */
public class Sound implements ISoundEventAccessor<Sound> {
	private final ResourceLocation name;
	private final float volume;
	private final float pitch;
	private final int weight;
	private final Sound.Type type;
	private final boolean streaming;
	private ResourceLocation oggLocation;

	public Sound(String nameIn, float volumeIn, float pitchIn, int weightIn, Sound.Type typeIn, boolean p_i46526_6_) {
		this.name = new ResourceLocation(nameIn);
		this.volume = volumeIn;
		this.pitch = pitchIn;
		this.weight = weightIn;
		this.type = typeIn;
		this.streaming = p_i46526_6_;
	}
	
	public ResourceLocation getSoundLocation() {
		return this.name;
	}
	
	public ResourceLocation getSoundAsOggLocation() {
		if (this.oggLocation == null) {
			this.oggLocation = this.resolveOggLocation();
		}
		return this.oggLocation;
	}

	private ResourceLocation resolveOggLocation() {
		ResourceLocation id = new ResourceLocation(this.name.getResourceDomain(), "sounds/" + this.name.getResourcePath() + ".ogg");
		if (soundResourceExists(id)) {
			return id;
		}
		ResourceLocation rawId = new ResourceLocation(this.name.getResourceDomain(), this.name.getResourcePath() + ".ogg");
		return soundResourceExists(rawId) ? rawId : id;
	}

	private static boolean soundResourceExists(ResourceLocation location) {
		Minecraft client = Minecraft.getMinecraft();
		try {
			Resource resource = client.getResourceManager().getResource(location);
			IOUtils.closeQuietly(resource.getInputStream());
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public float getVolume() {
		return this.volume;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public Sound cloneEntry() {
		return this;
	}
	
	public Sound.Type getType() {
		return this.type;
	}
	
	public boolean isStreaming() {
		return this.streaming;
	}
	
	public static enum Type {
		FILE("file"),
		SOUND_EVENT("event");
		
		private final String name;
		
		private Type(String nameIn) {
			this.name = nameIn;
		}
		
		public static Sound.Type getByName(String nameIn) {
			for (Sound.Type sound$type : values()) {
				if (sound$type.name.equals(nameIn)) {
					return sound$type;
				}
			}
			
			return null;
		}
	}
}