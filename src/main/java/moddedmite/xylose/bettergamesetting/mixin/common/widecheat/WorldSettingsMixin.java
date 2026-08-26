package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldSettings.class)
public class WorldSettingsMixin {
    @Shadow private boolean commandsAllowed;

    @Inject(method = "areCommandsAllowed", at = @At("TAIL"), cancellable = true)
    public void wide(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((BGSConfig.freeDevAllowCheat.get() && commandsAllowed));
    }
}
