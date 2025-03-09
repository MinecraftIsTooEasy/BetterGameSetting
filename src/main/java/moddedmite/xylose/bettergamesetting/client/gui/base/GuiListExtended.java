package moddedmite.xylose.bettergamesetting.client.gui.base;

import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import net.minecraft.*;
import org.lwjgl.input.Mouse;

public abstract class GuiListExtended extends GuiSlot {

    public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick) {
        Minecraft.getMinecraft().sndManager.playSoundFX("random.click", 1.0F, 1.0F);
    }

    protected boolean isSelected(int slotIndex) {
        return false;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int mouseXIn = Mouse.getX() * width / mc.displayWidth;
        int mouseYIn = height - Mouse.getY() * height / mc.displayHeight - 1;
        if (this.getListEntry(par1) != null)
            this.getListEntry(par1).drawEntry(par1, par2, par3, ((IGuiSlot) this).getListWidth(), par4, mouseXIn, mouseYIn, ((IGuiSlot) this).getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == par1);
    }

    protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
        this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        if (((IGuiSlot) this).isMouseYWithinSlotBounds(mouseY)) {
            int i = ((IGuiSlot) this).getSlotIndexFromScreenCoords(mouseX, mouseY);

            if (i >= 0) {
                int j = super.left + super.width / 2 - ((IGuiSlot) this).getListWidth() / 2 + 2;
                int k = (int) (this.top + 4 - super.amountScrolled + i * this.slotHeight + super.field_77242_t);
                int l = mouseX - j;
                int i1 = mouseY - k;

                try {
                    if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
//                    this.setEnabled(false);
                        return true;
                    }
                } catch (Exception ignored) {}
            }
        }

        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int mouseEvent) {
        for (int i = 0; i < this.getSize(); ++i) {
            int j = super.left + super.width / 2 - ((IGuiSlot) this).getListWidth() / 2 + 2;
            int k = (int) (this.top + 4 - this.amountScrolled + i * this.slotHeight + this.field_77242_t);
            int l = mouseX - j;
            int i1 = mouseY - k;
            this.getListEntry(i).mouseReleased(i, mouseX, mouseY, mouseEvent, l, i1);
        }

//        this.setEnabled(true);
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

