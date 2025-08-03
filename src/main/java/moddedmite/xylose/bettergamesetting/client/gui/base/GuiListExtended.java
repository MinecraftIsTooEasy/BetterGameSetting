package moddedmite.xylose.bettergamesetting.client.gui.base;

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
            this.getListEntry(slotIndex).drawEntry(slotIndex, x, y, this.getListWidth(), slotHeight, mouseX, mouseY, this.getSlotIndexFromScreenCoords(mouseX, mouseY) == slotIndex);
    }

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
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
                        Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
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

    public abstract GuiListExtended.IGuiListEntry getListEntry(int index);

    public interface IGuiListEntry {
        void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_);

        void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected);

        boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);

        void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);
    }
}

