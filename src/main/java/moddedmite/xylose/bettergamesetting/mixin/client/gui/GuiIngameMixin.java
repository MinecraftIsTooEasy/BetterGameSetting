package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.GuiSubtitleOverlay;
import net.minecraft.GuiIngame;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @Final @Shadow private Minecraft mc;

    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    private void renderSubtitles(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci) {
        GuiSubtitleOverlay overlay = this.mc.getGuiSubtitleOverlay();
        if (overlay != null) {
            overlay.renderSubtitles(new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight));
        }
    }
}
