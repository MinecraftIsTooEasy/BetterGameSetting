package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.FontRenderer;

public interface IButton {
    default void setPosition(int x, int y) {
    }

    default void setSize(int width, int height) {
    }

    /**
    * scrolling string
    */
    default void renderString(FontRenderer font, int color) {
    }
}
