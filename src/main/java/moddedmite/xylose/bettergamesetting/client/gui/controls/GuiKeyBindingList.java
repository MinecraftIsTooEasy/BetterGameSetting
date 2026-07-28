package moddedmite.xylose.bettergamesetting.client.gui.controls;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.keybinding.KeybindingV1;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiKeyBindingList extends GuiListExtended {
    private final GuiNewControls guiControls;
    private final Minecraft mc;
    public final List<IGuiListEntry> listEntries;
    private int maxListLabelWidth = 0;

    public GuiKeyBindingList(GuiNewControls controls, Minecraft mcIn) {
        super(mcIn, controls.width, controls.height, 63, controls.height - 32, 20);
        this.guiControls = controls;
        this.mc = mcIn;

        Map<KeybindingV1.Category, List<KeybindingV1>> map = Handlers.Keybinding.streamKeybindings()
                .sorted()// not needed on ric 1.5.7+
                .collect(Collectors.groupingBy(KeybindingV1::getCategory));

        this.listEntries = new ArrayList<>(map.size() + Handlers.Keybinding.streamKeybindings().toList().size());

        for (KeybindingV1.Category category : KeybindingV1.Category.SORT_ORDER) {
            if (map.containsKey(category)) {
                this.listEntries.add(new CategoryEntry(category));
                for (KeybindingV1 key : map.get(category)) {
                    this.listEntries.add(new KeyEntry(key));
                }
            }
        }

        this.maxListLabelWidth = Handlers.Keybinding.streamKeybindings()
                .mapToInt(x -> mcIn.fontRenderer.getStringWidth(I18n.getString(x.getName())))
                .max().orElse(0);
    }

    public int getSize() {
        return this.listEntries.size();
    }

//    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator) {
//        this.getListEntry(par1).drawEntry(par1, par2, par3, super.width, super.slotHeight, Mouse.getX(), Mouse.getY(), ((IGuiSlot) this).getSlotIndexFromScreenCoords(Mouse.getY(), Mouse.getX()) == par1);
//    }

    public IGuiListEntry getListEntry(int index) {
        return this.listEntries.get(index);
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry implements IGuiListEntry {
        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(KeybindingV1.Category category) {
            ResourceLocation id = category.id();
            this.labelText = I18n.getString(String.format("key.category.%s.%s", id.getResourceDomain(), id.getResourcePath()));
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

        @Override
        public void keyTyped(int slotIndex, char typedChar, int keyCode) {
        }

        public void setSelected(int slotIndex, int mouseX, int mouseY) {
        }
    }

    public class KeyEntry implements IGuiListEntry {
        private final KeybindingV1 keybinding;
        private final String keyDesc;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;

        private KeyEntry(KeybindingV1 key) {
            this.keybinding = key;
            this.keyDesc = I18n.getString(key.keyDescription);
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 20, I18n.getString(key.keyDescription));
            this.btnReset = new GuiButton(0, 0, 0, 50, 20, I18n.getString("controls.reset"));
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            boolean flag = GuiKeyBindingList.this.guiControls.binding == this.keybinding;
            GuiKeyBindingList.this.mc.fontRenderer.drawString(this.keyDesc, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + slotHeight / 2 - GuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = x + 190;
            this.btnReset.yPosition = y;
            this.btnReset.enabled = this.keybinding.keyCode != keybinding.getDefaultKey();
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
                GuiKeyBindingList.this.guiControls.binding = this.keybinding;
                return true;
            } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.mc, x, y)) {
                GuiKeyBindingList.this.mc.gameSettings.setOptionKeyBinding(this.keybinding, keybinding.getDefaultKey());
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

        @Override
        public void keyTyped(int slotIndex, char typedChar, int keyCode) {
        }

        public void setSelected(int slotIndex, int mouseX, int mouseY) {
        }
    }
}
