package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelectionEntry;
import net.minecraft.SaveFormatComparator;

import java.util.List;

public interface IGuiSelectWorld {
    List<SaveFormatComparator> getSaveList();

    void selectWorld(GuiListWorldSelectionEntry entry);

    void setVersionTooltip(String text);

    void loadWorld(String fileName);
}
