package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.BufferBuilder;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.util.GuiScreenPanoramaHelp;
import net.minecraft.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoadingScreenRenderer.class)
public class LoadingScreenRendererMixin {
    @Shadow private Minecraft mc;

    @WrapWithCondition(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClear(I)V", ordinal = 0))
    private boolean transparentBackgroundGLClear(int mask) {
        return !((IGameSetting) mc.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glClear(I)V", ordinal = 1))
    private boolean transparentBackgroundGLClear_1(int mask) {
        if (mc.currentScreen == null) return false;
        if (((IGameSetting) mc.gameSettings).isTransparentBackground()) {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GuiScreenPanoramaHelp.drawPanorama(mc.currentScreen);
            Gui.drawRect(0, 0, mc.currentScreen.width, mc.currentScreen.height, 1140850688);
        }
        return !((IGameSetting) mc.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentBackgroundBindTexture(TextureManager instance, ResourceLocation par1ResourceLocation) {
        return !((IGameSetting) mc.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V"))
    private boolean transparentBackgroundStart(Tessellator instance) {
        return !((IGameSetting) mc.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "setLoadingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I"))
    private boolean transparentBackgroundEnd(Tessellator instance) {
        return !((IGameSetting) mc.gameSettings).isTransparentBackground();
    }
}