package moddedmite.xylose.bettergamesetting.client.gui.controls;

import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public class GuiKeyBindingList extends GuiListExtended {
    private final GuiNewControls field_148191_k;
    private final Minecraft mc;
    public final IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;

    public GuiKeyBindingList(GuiNewControls controls, Minecraft mcIn) {
        super(mcIn, controls.width, controls.height, 63, controls.height - 32, 20);
        ((IGuiSlot) this).setListWidth(((IGuiSlot) this).getListWidth() + 32);
        this.field_148191_k = controls;
        this.mc = mcIn;
        KeyBinding[] akeybinding = (KeyBinding[]) ArrayUtils.clone(mcIn.gameSettings.keyBindings);
        this.listEntries = new IGuiListEntry[(int) (akeybinding.length + KeyBinding.keybindArray.size() / 1.5)];
        try {
            Arrays.sort((KeyBindingExtra[]) akeybinding);
        } catch (Exception ignored) {
        }
        int i = 0;
        String s = null;

        for (KeyBinding keybinding : akeybinding) {
            String s1 = KeyBindingExtra.getKeyCategory(keybinding.keyDescription);
//            String s1 = keybinding.keyDescription;

            if (!s1.equals(s)) {
                s = s1;
                this.listEntries[i++] = new CategoryEntry(s1);
            }

            int j = mcIn.fontRenderer.getStringWidth(I18n.getString(keybinding.keyDescription));

            if (j > this.maxListLabelWidth) {
                this.maxListLabelWidth = j;
            }

            this.listEntries[i++] = new KeyEntry(keybinding);
        }
    }

    public int getSize() {
        return this.listEntries.length;
    }

//    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
//        this.getListEntry(par1).drawEntry(par1, par2, par3, super.width, super.slotHeight, Mouse.getX(), Mouse.getY(), ((IGuiSlot) this).getSlotIndexFromScreenCoords(Mouse.getY(), Mouse.getX()) == par1);
//    }

    public IGuiListEntry getListEntry(int index) {
        return this.listEntries[index];
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

//    public int getListWidth() {
//        return super.getListWidth() + 32;
//    }

    public class CategoryEntry implements IGuiListEntry {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String p_i45028_2_) {
            this.labelText = I18n.getString(p_i45028_2_);
            this.labelWidth = GuiKeyBindingList.this.mc.fontRenderer.getStringWidth(this.labelText);
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.labelText, GuiKeyBindingList.this.mc.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }

        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            return false;
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }
    }

    public class KeyEntry implements IGuiListEntry {
        private final KeyBinding keybinding;
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;

        private KeyEntry(KeyBinding p_i45029_2_) {
            this.keybinding = p_i45029_2_;
            this.keyDesc = I18n.getString(p_i45029_2_.keyDescription);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.getString(p_i45029_2_.keyDescription));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.getString("controls.reset"));
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            boolean flag = GuiKeyBindingList.this.field_148191_k.buttonId == this.keybinding;
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = x + 190;
            this.btnReset.yPosition = y;
            this.btnReset.enabled = this.keybinding.keyCode != ((IKeyBinding) keybinding).getDefaultKeyCode(keybinding.keyDescription, keybinding.keyCode);
            this.btnReset.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
            this.btnChangeKeyBinding.xPosition = x + 105;
            this.btnChangeKeyBinding.yPosition = y;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keybinding.keyCode);
            boolean flag1 = false;

            if (this.keybinding.keyCode != 0) {
                for (KeyBinding keybinding : GuiKeyBindingList.this.mc.gameSettings.keyBindings) {
                    if (keybinding != this.keybinding && keybinding.keyCode == this.keybinding.keyCode) {
                        flag1 = true;
                        break;
                    }
                }
            }

            if (flag) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
            } else if (flag1) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.mc, mouseX, mouseY);
        }

        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, x, y)) {
                GuiKeyBindingList.this.field_148191_k.buttonId = this.keybinding;
                return true;
            } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, x, y)) {
                ((IGameSetting) GuiKeyBindingList.this.mc.gameSettings).setOptionKeyBinding(this.keybinding, ((IKeyBinding) keybinding).getDefaultKeyCode(keybinding.keyDescription, keybinding.keyCode));
                KeyBinding.resetKeyBindingArrayAndHash();
                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnReset.mouseReleased(x, y);
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }
    }
}
