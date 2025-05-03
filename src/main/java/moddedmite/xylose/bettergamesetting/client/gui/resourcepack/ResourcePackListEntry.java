package moddedmite.xylose.bettergamesetting.client.gui.resourcepack;

import java.util.List;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiExtra;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public abstract class ResourcePackListEntry extends GuiScreen implements GuiListExtended.IGuiListEntry {
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation(BGSClient.resourceId, "textures/gui/resource_packs.png");
    private static final ChatMessageComponent field_183020_d = ChatMessageComponent.createFromTranslationKey("resourcePack.incompatible");
    private static final ChatMessageComponent field_183021_e = ChatMessageComponent.createFromTranslationKey("resourcePack.incompatible.old");
    private static final ChatMessageComponent field_183022_f = ChatMessageComponent.createFromTranslationKey("resourcePack.incompatible.new");
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;

    public ResourcePackListEntry(GuiScreenResourcePacks resourcePacksGUIIn) {
        this.resourcePacksGUI = resourcePacksGUIIn;
        this.mc = Minecraft.getMinecraft();
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        int i = this.getPackFormat();

        if (i != 1) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
        }

        this.getPackIcon();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        String s = this.getPackName();
        String s1 = this.getPackDescription();

        if ((this.mc.gameSettings.touchscreen || isSelected) && this.func_148310_d()) {
            this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int k = mouseY - y;

            if (i < 1) {
                s = field_183020_d.toString();
                s1 = field_183021_e.toString();
            } else if (i > 1) {
                s = field_183020_d.toString();
                s1 = field_183022_f.toString();
            }

            if (this.hasNotPackEntry()) {
                if (j < 32) {
                    GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            } else {
                if (this.hasPackEntry()) {
                    if (j < 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148314_g()) {
                    if (j < 32 && j > 16 && k < 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }

                if (this.func_148307_h()) {
                    if (j < 32 && j > 16 && k > 16) {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                    } else {
                        GuiExtra.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                    }
                }
            }
        }

        int i1 = this.mc.fontRenderer.getStringWidth(s);

        if (i1 > 157) {
            s = this.mc.fontRenderer.trimStringToWidth(s, 157 - this.mc.fontRenderer.getStringWidth("...")) + "...";
        }

        this.mc.fontRenderer.drawStringWithShadow(s, (x + 32 + 2), (y + 1), 16777215);
        List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(s1, 157);

        for (int l = 0; l < 2 && l < list.size(); ++l) {
            this.mc.fontRenderer.drawStringWithShadow((String) list.get(l), (x + 32 + 2), (y + 12 + 10 * l), 8421504);
        }
    }

    protected abstract int getPackFormat();

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
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i > 0 && ((ResourcePackListEntry) list.get(i - 1)).func_148310_d();
    }

    protected boolean func_148307_h() {
        List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && ((ResourcePackListEntry) list.get(i + 1)).func_148310_d();
    }

    /**
     * Returns true if the mouse has been pressed on this control.
     */
    public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
        if (this.func_148310_d() && p_148278_5_ <= 32) {
            if (this.hasNotPackEntry()) {
                this.resourcePacksGUI.markChanged();
                int j = this.getPackFormat();

//                if (j != 1) {
//                    String s1 = I18n.getString("resourcePack.incompatible.confirm.title");
//                    String s = I18n.getString("resourcePack.incompatible.confirm." + (j > 1 ? "new" : "old"));
////                    this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
////                        public void confirmClicked(boolean result, int id) {
////                            List<ResourcePackListEntry> list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
////                            ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
////
////                            if (result) {
////                                list2.remove(ResourcePackListEntry.this);
////                                ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, ResourcePackListEntry.this);
////                            }
////                        }
////                    }, s1, s, 0));
//                } else {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
//                }

                return true;
            }

            if (p_148278_5_ < 16 && this.hasPackEntry()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }

            if (p_148278_5_ > 16 && p_148278_6_ < 16 && this.func_148314_g()) {
                List<ResourcePackListEntry> list1 = this.resourcePacksGUI.getListContaining(this);
                int k = list1.indexOf(this);
                list1.remove(this);
                list1.add(k - 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }

            if (p_148278_5_ > 16 && p_148278_6_ > 16 && this.func_148307_h()) {
                List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
                int i = list.indexOf(this);
                list.remove(this);
                list.add(i + 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
        }

        return false;
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}

    /**
     * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
     */
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
}
