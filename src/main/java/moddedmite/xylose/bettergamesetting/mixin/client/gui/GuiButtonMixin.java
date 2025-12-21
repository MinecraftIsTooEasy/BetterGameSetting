package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.IButton;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(GuiButton.class)
public class GuiButtonMixin extends Gui implements IButton {
    @Shadow public int xPosition;
    @Shadow public int yPosition;
    @Shadow public int width;
    @Shadow public int height;
    @Shadow public boolean enabled;
    @Shadow public String displayString;

    @ModifyConstant(method = "drawButton", constant = @Constant(intValue = 0xFFFFA0))
    private int highLightButtonText(int constant) {
        return Minecraft.getMinecraft().gameSettings.isHighlightButtonText() ? constant : 0xFFFFFF;
    }

    @Override
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Redirect(method = "drawButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/GuiButton;drawCenteredString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V"
            )
    )
    private void redirectDrawString(GuiButton instance, FontRenderer fontRenderer, String text, int x, int y, int color) {
        this.renderString(fontRenderer, color);
    }


    @Override
    public void renderString(FontRenderer font, int color) {
        this.renderScrollingString(font, 2, color);
    }

    @Unique
    private void renderScrollingString(FontRenderer font, int margin, int color) {
        int left = this.xPosition + margin;
        int right = this.xPosition + this.width - margin;
        ScreenUtil.getInstance().renderScrollingString(font, this.displayString, left, this.yPosition, right, this.yPosition + this.height, color);
    }
}
