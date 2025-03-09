package moddedmite.xylose.bettergamesetting.mixin.common.client;

import moddedmite.xylose.bettergamesetting.api.IEnumOptions;
import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import net.minecraft.EnumOptions;
import net.minecraft.MathHelper;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EnumOptions.class)
public abstract class EnumOptionsMixin implements IEnumOptions {
    @Final @Shadow public static EnumOptions RENDER_DISTANCE;
    @Final @Shadow public static EnumOptions FRAMERATE_LIMIT;
    @Final @Shadow public static EnumOptions GAMMA;
    @Final @Shadow public static EnumOptions GUI_SCALE;

    @Unique public float valueStep;
    @Unique private float valueMin;
    @Unique private float valueMax;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setMinMaxStepValue(CallbackInfo ci) {
        GAMMA.setValueMin(0.0F);
        GAMMA.setValueMax(1.0F);
        GAMMA.setValueStep(0.01F);
        RENDER_DISTANCE.setValueMin(2.0F);
        RENDER_DISTANCE.setValueMax(16.0F);
        RENDER_DISTANCE.setValueStep(1.0F);
        FRAMERATE_LIMIT.setValueMin(10.0F);
        FRAMERATE_LIMIT.setValueMax(260.0F);
        FRAMERATE_LIMIT.setValueStep(10.0F);
        GUI_SCALE.setValueMin(0.0F);
        GUI_SCALE.setValueMax(10.0F);
        GUI_SCALE.setValueStep(1.0F);
//        EnumOptionsExtra.MIPMAP_LEVELS.setValueMin(0.0F);
//        EnumOptionsExtra.MIPMAP_LEVELS.setValueMax(4.0F);
//        EnumOptionsExtra.MIPMAP_LEVELS.setValueStep(1.0F);
//        EnumOptionsExtra.ANISOTROPIC_FILTERING.setValueMin(1.0F);
//        EnumOptionsExtra.ANISOTROPIC_FILTERING.setValueMax(16.0F);
//        EnumOptionsExtra.ANISOTROPIC_FILTERING.setValueStep(1.0F);
    }


    @Inject(method = "getEnumFloat", at = @At("HEAD"), cancellable = true)
    private void changeToEnumFloat(CallbackInfoReturnable<Boolean> cir) {
        if (ReflectHelper.dyCast(this) == RENDER_DISTANCE) cir.setReturnValue(true);
        if (ReflectHelper.dyCast(this) == FRAMERATE_LIMIT) cir.setReturnValue(true);
        if (ReflectHelper.dyCast(this) == GUI_SCALE) cir.setReturnValue(true);
    }

    @Override
    public float normalizeValue(float value, EnumOptions options) {
        return MathHelper.clamp_float((this.snapToStepClamp(value, options) - options.getValueMin()) / (options.getValueMax() - options.getValueMin()), 0.0F, 1.0F);
    }

    @Override
    public float denormalizeValue(float value, EnumOptions options) {
        return this.snapToStepClamp(options.getValueMin() + (options.getValueMax() - options.getValueMin()) * MathHelper.clamp_float(value, 0.0F, 1.0F), options);
    }

    @Unique
    public float snapToStepClamp(float value, EnumOptions options) {
        value = this.snapToStep(value, options);
        return MathHelper.clamp_float(value, options.getValueMin(), options.getValueMax());
    }

    @Unique
    protected float snapToStep(float value, EnumOptions options) {
        if (options.getValueStep() > 0.0F) {
            value = options.getValueStep() * (float) Math.round(value / options.getValueStep());
        }
        return value;
    }

    @Override
    public float getValueMax() {
        return this.valueMax;
    }

    @Override
    public void setValueMax(float valueMax) {
        this.valueMax = valueMax;
    }

    @Override
    public float getValueMin() {
        return this.valueMin;
    }

    @Override
    public void setValueMin(float valueMin) {
        this.valueMin = valueMin;
    }

    @Override
    public float getValueStep() {
        return this.valueMin;
    }

    @Override
    public void setValueStep(float valueStep) {
        this.valueStep = valueStep;
    }
}
