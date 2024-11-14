package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.KeyBinding;

public interface IKeyBinding {
    default String getKeyCategory() {
        return "";
    }

    default int compareTo(KeyBinding p_compareTo_1_) {
        return 0;
    }

    default int getKeyCode() {
        return 0;
    }

    default void setKeyCode(int keyCode) {
        return;
    }
}
