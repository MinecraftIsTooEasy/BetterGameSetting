package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.GuiButton;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Map;

public interface IGuiCreateWorld {
    Map<Integer, String> bgs$getHoverTexts();
    int bgs$getCurrentTab();
    List<GuiButton> bgs$getTabButtons();
    void switchSkillsEnable();
    boolean isSkillsEnable();
}
