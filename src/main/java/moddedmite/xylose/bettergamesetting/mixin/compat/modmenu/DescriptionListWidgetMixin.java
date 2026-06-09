package moddedmite.xylose.bettergamesetting.mixin.compat.modmenu;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.terraformersmc.modmenu.gui.widget.DescriptionListWidget;
import com.terraformersmc.modmenu.gui.widget.entries.EntryListWidget;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition("modmenu"))
@Mixin(DescriptionListWidget.class)
public abstract class DescriptionListWidgetMixin extends EntryListWidget {
    @Shadow @Final private Minecraft minecraft;

    public DescriptionListWidgetMixin(Minecraft minecraft, int width, int height, int top, int bottom, int slotHeight) {
        super(minecraft, width, height, top, bottom, slotHeight);
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    private boolean transparentBackground(TextureManager instance, ResourceLocation par1ResourceLocation) {
        if (minecraft.gameSettings.isTransparentBackground()) {
            Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
            ScreenUtil.scissorHead(this.left, this.top, this.right, this.bottom - this.top);
        }
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start(I)V", ordinal = 0))
    private boolean transparentBackgroundStart(BufferBuilder instance, int drawMode) {
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I", ordinal = 0))
    private boolean transparentBackgroundEnd(BufferBuilder instance) {
        if (minecraft.gameSettings.isTransparentBackground())
            ScreenUtil.scissorTail();
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start(I)V", ordinal = 1))
    private boolean delGradientMatteStart_1(BufferBuilder instance, int drawMode) {
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I", ordinal = 1))
    private boolean delGradientMatteEnd_1(BufferBuilder instance) {
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;start(I)V", ordinal = 2))
    private boolean delGradientMatteStart_2(BufferBuilder instance, int drawMode) {
        return !minecraft.gameSettings.isTransparentBackground();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;end()I", ordinal = 2))
    private boolean delGradientMatteEnd_2(BufferBuilder instance) {
        return !minecraft.gameSettings.isTransparentBackground();
    }
}
