package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.KeyBinding;

public interface IKeyBinding extends Comparable<KeyBinding> {
    default String getKeyCategory(String keyDescription) {
        return "";
    }

    default int getKeyCode() {
        return 0;
    }

    default void setKeyCode(int keyCode) {
    }

    default int getDefaultKeyCode(String keyDescription) {
        return 0;
    }
}
