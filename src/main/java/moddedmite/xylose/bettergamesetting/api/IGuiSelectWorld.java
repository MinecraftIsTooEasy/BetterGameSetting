package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.SaveFormatComparator;

import java.util.List;

public interface IGuiSelectWorld {
    List<SaveFormatComparator> getSaveList();

    int getSelectedIndex();

    void onElementClicked(int index, boolean isDoubleClick);
}
