package moddedmite.xylose.bettergamesetting.client.gui.world;

import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.SaveFormatComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuiListWorldSelection extends GuiListExtended {
    private final GuiScreen parent;
    private final IGuiSelectWorld worldSelection;
    private final List<GuiListWorldSelectionEntry> entries = new ArrayList<>();
    private int selectedIdx = -1;

    public GuiListWorldSelection(GuiScreen parent, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.worldSelection = (IGuiSelectWorld) parent;
        this.centerListVertically = false;
        this.refreshList();
    }

    public void refreshList() {
        this.entries.clear();
	    for (SaveFormatComparator save : this.worldSelection.getSaveList()) {
		    this.entries.add(new GuiListWorldSelectionEntry(this, save));
	    }
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }

    @Override
    public GuiListWorldSelectionEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    protected void drawBackground() {
        this.parent.drawDefaultBackground();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        int k = this.top + 4 - this.getAmountScrolled() + slotIndex * this.slotHeight + this.headerPadding;
        this.getListEntry(slotIndex).mousePressed(slotIndex, mouseX, mouseY, 0, mouseX - j, mouseY - k);
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.selectedIdx;
    }

    @Override
    public int getListWidth() {
        return 250;
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 10;
    }

    public void selectWorld(int idx) {
        this.selectedIdx = idx;
        this.worldSelection.selectWorld(this.getSelectedWorld());
    }

    public GuiListWorldSelectionEntry getSelectedWorld() {
        return this.selectedIdx >= 0 && this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
    }

    public IGuiSelectWorld getGuiWorldSelection() {
        return this.worldSelection;
    }

    public GuiScreen getParentScreen() {
        return this.parent;
    }

    public static File getIconFile(String folderName) {
        return new File(new File(Minecraft.getMinecraft().saves_dir_MITE, folderName), "icon.png");
    }
}
