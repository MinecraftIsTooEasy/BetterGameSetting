package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiSlot.class, priority = 1200)
public abstract class GuiSlotMixin {
    @Shadow public int width;
    @Shadow protected int top;
    @Shadow protected int bottom;
    @Shadow public int right;
    @Shadow public int left;
    @Shadow @Final private Minecraft mc;

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentBackground(TextureManager instance, ResourceLocation par1ResourceLocation) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 0))
    private boolean transparentBackgroundStart(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 0))
    private boolean transparentBackgroundEnd(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 1))
    private boolean delGradientMatteStart(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 1))
    private boolean delGradientMatteEnd(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 2))
    private boolean delGradientMatteStart1(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawDarkenedBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 2))
    private boolean delGradientMatteEnd1(Tessellator instance) {
        return !mc.gameSettings.isTransparentBackground();
    }

    @Inject(method = "overlayBackground", at = @At("HEAD"), cancellable = true)
    private void transparentOverlayBackground(int j, int k, int l, int par4, CallbackInfo ci) {
        if (mc.gameSettings.isTransparentBackground()) {
            ci.cancel();
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiSlot;drawDarkenedBackground(I)V", ordinal = 0))
    private void scissorSlotStart(int j, int f, float par3, CallbackInfo ci) {
        Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glScissor((this.left * sr.getScaleFactor()), (mc.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiSlot;drawDarkenedBackground(I)V", ordinal = 1))
    private void scissorSlotEnd(int j, int f, float par3, CallbackInfo ci) {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
