package moddedmite.xylose.bettergamesetting.mixin.common.client.compat.modmenu;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.terraformersmc.modmenu.gui.widget.entries.EntryListWidget;
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

@Restriction(require = @Condition("modmenu"))
@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin extends GuiSlot {
    @Shadow protected abstract void renderList(int x, int y, int mouseX, int mouseY);

    @Unique private Minecraft client;

    public EntryListWidgetMixin(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6) {
        super(par1Minecraft, par2, par3, par4, par5, par6);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addClientField(Minecraft minecraft, int width, int height, int top, int bottom, int slotHeight, CallbackInfo ci) {
        client = minecraft;
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentBackground(TextureManager instance, ResourceLocation par1ResourceLocation) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start()V", ordinal = 0))
    private boolean transparentBackgroundStart(BufferBuilder instance) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I", ordinal = 0))
    private boolean transparentBackgroundEnd(BufferBuilder instance) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start()V", ordinal = 1))
    private boolean delGradientMatteStart(BufferBuilder instance) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I", ordinal = 1))
    private boolean delGradientMatteEnd(BufferBuilder instance) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/gui/widget/entries/EntryListWidget;renderList(IIII)V"))
    private boolean delRenderList(EntryListWidget instance, int n5, int entryY, int slotHeight, int i) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/gui/widget/entries/EntryListWidget;renderHoleBackground(IIII)V", ordinal = 1, shift = At.Shift.AFTER))
    private void addRenderList(int mouseX, int mouseY, float tickDelta, CallbackInfo ci, @Local(name = "n4") int n4, @Local(name = "n5") int n5) {
        if (((IGameSetting) client.gameSettings).isTransparentBackground()) {
            ScaledResolution sr = new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);
            GL11.glScissor((this.left * sr.getScaleFactor()), (client.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            this.renderList(n5, n4, mouseX, mouseY);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }

    @WrapWithCondition(method = "renderHoleBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentHoleBackground(TextureManager instance, ResourceLocation par1ResourceLocation) {
        if (((IGameSetting) client.gameSettings).isTransparentBackground()) {
            Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
            ScaledResolution sr = new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);
            GL11.glScissor((this.left * sr.getScaleFactor()), (client.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "renderHoleBackground", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start()V"))
    private boolean transparentHoleBackgroundStart(BufferBuilder instance) {
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }

    @WrapWithCondition(method = "renderHoleBackground", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I"))
    private boolean transparentHoleBackgroundEnd(BufferBuilder instance) {
        if (((IGameSetting) client.gameSettings).isTransparentBackground())
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        return !((IGameSetting) client.gameSettings).isTransparentBackground();
    }
}
