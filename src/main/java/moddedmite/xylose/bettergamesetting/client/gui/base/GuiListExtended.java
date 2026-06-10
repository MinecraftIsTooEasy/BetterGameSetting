package moddedmite.xylose.bettergamesetting.client.gui.base;

import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;

public abstract class GuiListExtended extends GuiSlotModern {

    public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int slotIndex, int x, int y, int slotHeight, Tessellator tessellator, int mouseX, int mouseY) {
        if (this.getListEntry(slotIndex) != null)
            ScreenUtil.scissorExecute(this.left, this.top, this.width, this.bottom - this.top, () -> {
                this.getListEntry(slotIndex).drawEntry(slotIndex, x, y, this.getListWidth(), slotHeight, mouseX, mouseY, this.getSlotIndexFromScreenCoords(mouseX, mouseY) == slotIndex);
            });
    }

    protected void selectedEntry(int slotIndex, int mouseX, int mouseY) {
        this.getListEntry(slotIndex).setSelected(slotIndex, mouseX, mouseY);
    }

    protected void drawTooltip(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
    }

    public boolean mouseClicked(int mouseXIn, int mouseYIn, int mouseEvent) {
        if (this.isMouseYWithinSlotBounds(mouseYIn)) {
            int i = this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn);

            if (i >= 0) {
                int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
                int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
                int l = mouseXIn - j;
                int i1 = mouseYIn - k;

                try {
                    if (this.getListEntry(i).mousePressed(i, mouseXIn, mouseYIn, mouseEvent, l, i1)) {
                        this.setEnabled(false);
                        this.client.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                        return true;
                    }
                } catch (Exception ignore) {}
            }
        }

        return false;
    }

    public boolean mouseReleased(int mouseXIn, int mouseYIn, int mouseEvent) {
        for (int i = 0; i < this.getSize(); ++i) {
            int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            int l = mouseXIn - j;
            int i1 = mouseYIn - k;
            this.getListEntry(i).mouseReleased(i, mouseXIn, mouseYIn, mouseEvent, l, i1);
        }

        this.setEnabled(true);
        return false;
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (int i = 0; i < this.getSize(); ++i) {
            this.getListEntry(i).keyTyped(i, typedChar, keyCode);
        }
    }

    public abstract GuiListExtended.IGuiListEntry getListEntry(int index);

    public interface IGuiListEntry {
        void setSelected(int slotIndex, int mouseX, int mouseY);

        void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected);

        boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);

        void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);

        void keyTyped(int slotIndex, char typedChar, int keyCode);
    }
}

