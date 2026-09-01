package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.ResourceLocation;
import net.minecraft.SoundPoolEntry;

public interface ISoundPoolEntry {
	default ResourceLocation getSoundPoolEntryLocation() {
		return null;
	}
	
	default SoundPoolEntry cloneEntry() {
		return null;
	}

	default double getPitch() {
		return 0.0F;
	}

	default void setPitch(double pitch) {
	}

	default double getVolume() {
		return  0.0;
	}

	default void setVolume(double volume) {
	}

	default boolean isStreaming() {
		return false;
	}

	default void setStreaming(boolean streaming) {
	}
}
