package moddedmite.xylose.bettergamesetting.mixin.common.client.compat.emi;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import shims.java.net.minecraft.client.gui.widget.ButtonWidget;

@Restriction(require = @Condition("emi"))
@Mixin(ButtonWidget.class)
public class ButtonWidgetMixin {
    @ModifyConstant(method = "renderWidget", constant = @Constant(intValue = 268435360))
    private int highLightButtonText(int constant) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).isHighlightButtonText() ? constant : 0xFFFFFF;
    }
}
