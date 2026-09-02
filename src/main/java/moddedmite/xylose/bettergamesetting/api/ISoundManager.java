package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.ISound;
import moddedmite.xylose.bettergamesetting.client.audio.ISoundEventListener;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import net.minecraft.Entity;
import net.minecraft.EntityPlayer;

public interface ISoundManager {
	default void playSound(ISound sound) {
	}

	default void updateAllSounds() {
	}

	default void unloadSoundSystem() {
	}

	default void stopSound(ISound sound) {
	}

	default boolean isSoundPlaying(ISound sound) {
		return false;
	}

	default void setVolume(SoundCategory category, float volume) {
	}

	default float getVolume(SoundCategory category) {
		return 1.0F;
	}

	default void addDelayedSound(ISound sound, int delay) {
	}
	
	default void reloadSoundSystem() {
	}
	
	default void setListener(EntityPlayer player, float multiplier) {
	}
	
	default void setListener(Entity player, float multiplier) {
	}

	default void addListener(ISoundEventListener listener) {
	}
	
	default void removeListener(ISoundEventListener listener) {
	}
	
	default void stop(String soundId, SoundCategory category) {
	}
}
