package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.EnumChatFormatting;
import net.minecraft.Minecraft;
import net.minecraft.Tessellator;

import java.util.List;

public abstract class GuiResourcePackList extends GuiListExtended {
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> resourcePacksGUI;

    public GuiResourcePackList(Minecraft mcIn, int widthIn, int heightIn, List<ResourcePackListEntry> resourcePackList) {
        super(mcIn, widthIn, heightIn, 32, heightIn - 55 + 4, 36);
        ((IGuiSlot) this).setListWidth(this.width);
        this.mc = mcIn;
        this.resourcePacksGUI = resourcePackList;
//        this.field_148163_i = false;
        this.func_77223_a(true, (int) ((float) mcIn.fontRenderer.FONT_HEIGHT * 1.5F));
    }

    /**
     * Handles drawing a list's header row.
     */
    @Override
    public void func_77222_a(int x, int y, Tessellator tessellator) {
        String s = EnumChatFormatting.UNDERLINE + "" + EnumChatFormatting.BOLD + this.getListHeader();
        this.mc.fontRenderer.drawString(s, x + this.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, Math.min(this.top + 3, y), 16777215);
    }

    protected abstract String getListHeader();

    public List<ResourcePackListEntry> getResourcePackList() {
        return this.resourcePacksGUI;
    }

    protected int getSize() {
        return this.getResourcePackList().size();
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public ResourcePackListEntry getListEntry(int p_148180_1_) {
        return this.getResourcePackList().get(p_148180_1_);
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return this.width;
    }

    protected int getScrollBarX() {
        return this.right - 6;
    }
}