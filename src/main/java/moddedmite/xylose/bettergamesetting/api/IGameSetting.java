package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.KeyBinding;

import java.util.List;

public interface IGameSetting {
    default void setOptionKeyBinding(KeyBinding key, int keyCode) {
    }

    default float getRecordVolume() {
        return 0.0F;
    }

    default float getWeatherVolume() {
        return 0.0F;
    }

    default float getBlockVolume() {
        return 0.0F;
    }

    default float getHostileVolume() {
        return 0.0F;
    }

    default float getNeutralVolume() {
        return 0.0F;
    }

    default float getPlayerVolume() {
        return 0.0F;
    }

    default float getAmbientVolume() {
        return 0.0F;
    }

    default List<String> getResourcePacks() {
        return null;
    }
}
