package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Gui;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiButton.class)
public class GuiButtonMixin extends Gui {
    @ModifyConstant(method = "drawButton", constant = @Constant(intValue = 0xFFFFA0))
    private int highLightButtonText(int constant) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).isHighlightButtonText() ? constant : 0xFFFFFF;
    }
}
