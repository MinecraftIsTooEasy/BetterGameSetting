package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiVideoSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public class GuiVideoSettingsMixin extends GuiScreen {
    @Inject(method = "initGui", at = @At(value = "TAIL"))
    private void inject(CallbackInfo ci) {
        for (Object o : this.buttonList) {
            ((GuiButton) o).enabled = true;
        }
    }
}
