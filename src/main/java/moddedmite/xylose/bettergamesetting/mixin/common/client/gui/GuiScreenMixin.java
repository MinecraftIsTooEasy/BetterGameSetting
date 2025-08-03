package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.util.GuiScreenPanoramaHelp;
import net.minecraft.Gui;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Shadow public int width;
    @Shadow public int height;
    @Shadow public Minecraft mc;

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    private void transparentBackground(int par1, CallbackInfo ci) {
        if (mc.currentScreen == null) return;
        if (mc.gameSettings.isTransparentBackground()) {
            ci.cancel();
            if (Minecraft.getMinecraft().theWorld == null) {
                GuiScreenPanoramaHelp.drawPanorama(ReflectHelper.dyCast(this));
                Gui.drawRect(0, 0, this.width, this.height, 0x44000000);
            } else {
                Gui.drawRect(0, 0, this.width, this.height, 0xAA000000);
            }
        }
    }
}
