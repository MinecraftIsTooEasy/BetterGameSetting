package moddedmite.xylose.bettergamesetting.mixin.common.client;

import net.minecraft.EnumOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EnumOptions.class)
public abstract class EnumOptionsMixin {
    @Shadow public abstract String getEnumString();

    @Inject(method = "getEnumFloat", at = @At("HEAD"), cancellable = true)
    private void changeToEnumFloat(CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(getEnumString(), "options.renderDistance")) cir.setReturnValue(true);
        if (Objects.equals(getEnumString(), "options.framerateLimit")) cir.setReturnValue(true);
        if (Objects.equals(getEnumString(), "options.guiScale")) cir.setReturnValue(true);
    }
}
