package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class GuiMultiplayerMixin extends GuiScreen {

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiSlotServer;drawScreen(IIF)V"))
    private void disableServerSlotDepthTest(int par1, int par2, float par3, CallbackInfo ci) {
        if (((IGameSetting) mc.gameSettings).isTransparentBackground())
            GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
