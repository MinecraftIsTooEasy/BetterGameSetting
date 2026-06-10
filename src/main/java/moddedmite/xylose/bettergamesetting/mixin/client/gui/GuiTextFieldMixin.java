package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.ITextField;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.FontRenderer;
import net.minecraft.GuiTextField;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public abstract class GuiTextFieldMixin implements ITextField {

    @Final @Mutable @Shadow public int xPos;
    @Final @Mutable @Shadow public int yPos;
    @Final @Mutable @Shadow private int width;
    @Final @Mutable @Shadow public int height;
    @Shadow @Final private FontRenderer fontRenderer;
    @Shadow private boolean visible;
    @Shadow public abstract boolean isFocused();
    @Shadow public abstract String getText();

    @Unique private String hint;

    public void setPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
    
    public boolean isMouseOver() {
        if (!this.visible) return false;
        Minecraft client = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);
        int mouseX = ScreenUtil.getMouseX(scaledresolution);
        int mouseY = ScreenUtil.getMouseY(scaledresolution);
        return mouseX >= this.xPos && mouseY >= this.yPos && mouseX < this.xPos + this.width && mouseY < this.yPos + this.height;
    }

    @Inject(method = "drawTextBox", at = @At("TAIL"))
    private void addHint(CallbackInfo ci) {
        if (this.hint != null && this.getText().isEmpty() && !this.isFocused()) {
            int x = this.xPos + 4;
            int y = this.yPos + (this.height - 8) / 2;
            this.fontRenderer.drawStringWithShadow(this.hint, x, y, 0x707070);
        }
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 0), index = 0)
    private int modifyBorderX1(int originalX1) {
        return originalX1 + 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 0), index = 1)
    private int modifyBorderY1(int originalY1) {
        return originalY1 + 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 0), index = 2)
    private int modifyBorderX2(int originalX2) {
        return originalX2 - 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 0), index = 3)
    private int modifyBorderY2(int originalY2) {
        return originalY2 - 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 1), index = 0)
    private int modifyBGX1(int originalX1) {
        return originalX1 + 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 1), index = 1)
    private int modifyBGY1(int originalY1) {
        return originalY1 + 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 1), index = 2)
    private int modifyBGX2(int originalX2) {
        return originalX2 - 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 1), index = 3)
    private int modifyBGY2(int originalY2) {
        return originalY2 - 1;
    }

    @ModifyArg(method = "drawTextBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiTextField;drawRect(IIIII)V", ordinal = 0), index = 4)
    private int modifyColorFocused(int original) {
        return (this.isFocused()) ? 0xFFFFFFFF : original;
    }
}