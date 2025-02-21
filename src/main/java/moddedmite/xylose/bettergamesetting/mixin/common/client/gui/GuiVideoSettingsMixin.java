package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public class GuiVideoSettingsMixin extends GuiScreen {
    @Inject(method = "initGui", at = @At(value = "TAIL"))
    private void enabledAll(CallbackInfo ci) {
        for (Object o : this.buttonList) {
            ((GuiButton) o).enabled = true;
        }
    }
}
