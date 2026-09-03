package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import net.minecraft.KeyBinding;

import java.util.List;

public interface IGameSetting {
    default void setOptionKeyBinding(KeyBinding key, int keyCode) {
    }

    default float getSoundLevel(SoundCategory category) {
        return 1.0F;
    }

    default void setSoundLevel(SoundCategory category, float volume) {
    }

    default List<String> getResourcePacks() {
        return null;
    }

    default List<String> getIncompatibleResourcePacks() {
        return null;
    }

    default boolean isForceUnicodeFont() {
        return false;
    }

    default boolean isTransparentBackground() {
        return false;
    }

    default boolean isHighlightButtonText() {
        return false;
    }

    default boolean isDeferChunkUpdates() {
        return false;
    }

    default String getSoundDevice() {
        return "";
    }

    default void setSoundDevice(String device) {
    }

    default boolean isDirectionalAudio() {
        return false;
    }

    default void setDirectionalAudio(boolean enabled) {
    }

    default boolean isShowSubtitles() {
        return false;
    }
}
