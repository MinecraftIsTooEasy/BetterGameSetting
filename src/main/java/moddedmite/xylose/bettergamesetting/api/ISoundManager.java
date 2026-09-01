package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.ISound;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;

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

	default void setSoundCategoryVolume(SoundCategory category, float volume) {
	}

	default float getSoundCategoryVolume(SoundCategory category) {
		return 1.0F;
	}

	default void addDelayedSound(ISound sound, int delay) {
	}
}
