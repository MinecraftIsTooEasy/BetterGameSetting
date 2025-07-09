package moddedmite.xylose.bettergamesetting.mixin.common.client.compat.emi;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.screen.widget.config.ListWidget;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shims.java.net.minecraft.client.gui.DrawContext;

@Restriction(require = @Condition("emi"))
@Mixin(ListWidget.class)
public abstract class ListWidgetMixin {

    @Shadow protected int left;
    @Shadow protected int top;
    @Shadow protected int right;
    @Shadow protected int bottom;

    @Shadow public abstract int getRowLeft();

    @Shadow public abstract double getScrollAmount();

    @Shadow protected abstract void renderList(DrawContext draw, int x, int y, int mouseX, int mouseY, float delta);

    @Unique private Minecraft client;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addClientField(Minecraft client, int width, int height, int top, int bottom, CallbackInfo ci) {
        this.client = client;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentBackgroundBindTexture(TextureManager instance, ResourceLocation par1ResourceLocation) {
        if (((IGameSetting) client.gameSettings).isTransparentBackground()) {
            Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
        }
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 0))
    private boolean transparentBackgroundStart(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 0))
    private boolean transparentBackgroundEnd(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 1))
    private boolean delGradientMatteStart(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 1))
    private boolean delGradientMatteEnd(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;startDrawingQuads()V", ordinal = 2))
    private boolean delGradientMatteStart1(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 2))
    private boolean delGradientMatteEnd1(Tessellator instance) {
        return !((IGameSetting) this.client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/widget/config/ListWidget;renderList(Lshims/java/net/minecraft/client/gui/DrawContext;IIIIF)V"))
    private boolean delRenderList(ListWidget instance, DrawContext f, int p, int o, int k, int l, float m) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;draw()I", ordinal = 1, shift = At.Shift.AFTER))
    private void addRenderList(DrawContext draw, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (((IGameSetting) client.gameSettings).isTransparentBackground()) {
            int k = this.getRowLeft();
            int l = this.top + 4 - (int)this.getScrollAmount();
            ScaledResolution sr = new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);
            GL11.glScissor((this.left * sr.getScaleFactor()), (client.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            this.renderList(draw, k, l, mouseX, mouseY, delta);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }
}
