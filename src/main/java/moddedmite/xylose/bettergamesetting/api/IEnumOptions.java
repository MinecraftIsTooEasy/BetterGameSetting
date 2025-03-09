package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.EnumOptions;
import net.minecraft.MathHelper;

public interface IEnumOptions {
    default float getValueMax() {
        return 0;
    }

    default void setValueMax(float valueMax) {
    }

    default float getValueMin() {
        return 0;
    }

    default void setValueMin(float valueMin) {
    }

    default float getValueStep() {
        return 0;
    }

    default void setValueStep(float valueStep) {
    }

    default float normalizeValue(float value, EnumOptions options) {
        return 0;
    }

    default float denormalizeValue(float value, EnumOptions options) {
        return 0;
    }
}
