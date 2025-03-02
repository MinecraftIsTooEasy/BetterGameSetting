package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import java.util.List;

import moddedmite.xylose.bettergamesetting.client.gui.GuiExtra;
import moddedmite.xylose.bettergamesetting.client.gui.GuiListExtended;
import moddedmite.xylose.bettergamesetting.init.BetterGameSettingClient;
import net.minecraft.Minecraft;
import net.minecraft.Gui;
import net.minecraft.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
    static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation(BetterGameSettingClient.modid, "textures/gui/resource_packs.png");
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;

    public ResourcePackListEntry(GuiScreenResourcePacks resourcePacksGUI) {
        this.resourcePacksGUI = resourcePacksGUI;
        this.mc = Minecraft.getMinecraft();
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        this.getPackIcon();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        int i2;

        if ((this.mc.gameSettings.touchscreen || isSelected) && this.func_148310_d()) {
            this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int l1 = mouseX - x;
            i2 = mouseY - y;

            if (this.hasNotPackEntry()) {
                if (l1 < 32) {
                    GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            } else {
                if (this.hasPackEntry()) {
                    if (l1 < 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148314_g()) {
                    if (l1 < 32 && l1 > 16 && i2 < 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148307_h()) {
                    if (l1 < 32 && l1 > 16 && i2 > 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }

        String s = this.getPackName();
        i2 = this.mc.fontRenderer.getStringWidth(s);

        if (i2 > 157) {
            s = this.mc.fontRenderer.trimStringToWidth(s, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }

        this.mc.fontRenderer.drawStringWithShadow(s, x + 32 + 2, y + 1, 16777215);
        List list = this.mc.fontRenderer.listFormattedStringToWidth(this.getPackDescription(), 157);

        for (int j2 = 0; j2 < 2 && j2 < list.size(); ++j2) {
            this.mc.fontRenderer.drawStringWithShadow((String) list.get(j2), x + 32 + 2, y + 12 + 10 * j2, 8421504);
        }
    }

    protected abstract String getPackDescription();

    protected abstract String getPackName();

    protected abstract void getPackIcon();

    protected boolean func_148310_d() {
        return true;
    }

    protected boolean hasNotPackEntry() {
        return !this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean hasPackEntry() {
        return this.resourcePacksGUI.hasResourcePackEntry(this);
    }

    protected boolean func_148314_g() {
        List list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i > 0 && ((ResourcePackListEntry) list.get(i - 1)).func_148310_d();
    }

    protected boolean func_148307_h() {
        List list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry) list.get(i + 1)).func_148310_d();
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        if (this.func_148310_d() && relativeX <= 32) {
            if (this.hasNotPackEntry()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
                return true;
            }

            if (relativeX < 16 && this.hasPackEntry()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                return true;
            }

            List list;
            int k1;

            if (relativeX > 16 && relativeY < 16 && this.func_148314_g()) {
                list = this.resourcePacksGUI.getListContaining(this);
                k1 = list.indexOf(this);
                list.remove(this);
                list.add(k1 - 1, this);
                return true;
            }

            if (relativeX > 16 && relativeY > 16 && this.func_148307_h()) {
                list = this.resourcePacksGUI.getListContaining(this);
                k1 = list.indexOf(this);
                list.remove(this);
                list.add(k1 + 1, this);
                return true;
            }
        }

        return false;
    }

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
}
