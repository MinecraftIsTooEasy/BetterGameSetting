package moddedmite.xylose.bettergamesetting.api;

public interface ITextField {
    default void setPosition(int x, int y) {
    }

    default void setSize(int width, int height) {
    }

    default void setHint(String hint) {
    }

    default boolean isMouseOver() {
        return false;
    }
}
