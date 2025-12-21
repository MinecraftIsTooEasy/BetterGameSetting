package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldSettings.class)
public class WorldSettingsMixin {
    @Shadow private boolean bonusChestEnabled;

    @Inject(method = "enableBonusChest", at = @At(value="RETURN"))
    private void enableBonusChestABLE(CallbackInfoReturnable<WorldSettings> cir) {
        this.bonusChestEnabled = true;
    }
}
