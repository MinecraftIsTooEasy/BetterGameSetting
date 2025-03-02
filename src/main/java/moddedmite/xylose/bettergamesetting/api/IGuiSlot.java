package moddedmite.xylose.bettergamesetting.api;


public interface IGuiSlot {
    default boolean isMouseYWithinSlotBounds(int p_148141_1_) {
        return false;
    }

    default int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_) {
        return 0;
    }

    default int getScrollBarX() {
        return 0;
    }

    default void handleMouseInput() {}

    default void setSlotXBoundsFromLeft(int p_148140_1_) {}

}
