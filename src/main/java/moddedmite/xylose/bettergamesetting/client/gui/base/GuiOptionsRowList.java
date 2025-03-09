package moddedmite.xylose.bettergamesetting.client.gui.base;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionSlider;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiScaleSlider;
import net.minecraft.EnumOptions;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;

import java.util.List;

public class GuiOptionsRowList extends GuiListExtended {
    private final List<GuiOptionsRowList.Row> optionsRowList = Lists.<GuiOptionsRowList.Row>newArrayList();

    public GuiOptionsRowList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn, EnumOptions... options) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
//        this.field_148163_i = false;
        ((IGuiSlot) this).setListWidth(400);

        for (int i = 0; i < options.length; i += 2) {
            EnumOptions gamesettings$options = options[i];
            EnumOptions gamesettings$options1 = i < options.length - 1 ? options[i + 1] : null;
            GuiButton guibutton = this.getOptionButtons(mcIn, widthIn / 2 - 155, 0, gamesettings$options);
            GuiButton guibutton1 = this.getOptionButtons(mcIn, widthIn / 2 - 155 + 160, 0, gamesettings$options1);
            this.optionsRowList.add(new GuiOptionsRowList.Row(guibutton, guibutton1));
        }
    }

    private GuiButton getOptionButtons(Minecraft mcIn, int x, int y, EnumOptions options) {
        if (options == null) {
            return null;
        } else if (options == EnumOptions.GUI_SCALE) {
            int j = 1000;
            int max = 1;

            while (max < j && mcIn.displayWidth / (max + 1) >= 320 && mcIn.displayHeight / (max + 1) >= 240) {
                ++max;
            }

            if (max != 1)
                max--;

            return new GuiScaleSlider(options.returnEnumOrdinal(), x, y, mcIn.gameSettings, 0, max);
        } else {
            int i = options.returnEnumOrdinal();
            return (GuiButton) (options.getEnumFloat() ? new GuiOptionSlider(i, x, y, options) : new GuiOptionButton(i, x, y, options, mcIn.gameSettings.getKeyBinding(options)));
        }
    }

    public GuiOptionsRowList.Row getListEntry(int index) {
        return (GuiOptionsRowList.Row) this.optionsRowList.get(index);
    }

    protected int getSize() {
        return this.optionsRowList.size();
    }

    public int getListWidth() {
        return 400;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    public static class Row implements GuiListExtended.IGuiListEntry {
        private final Minecraft minecraft = Minecraft.getMinecraft();
        private final GuiButton buttonLeft;
        private final GuiButton buttonRight;

        public Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_) {
            this.buttonLeft = p_i45014_1_;
            this.buttonRight = p_i45014_2_;
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            if (this.buttonLeft != null) {
                this.buttonLeft.yPosition = y;
                this.buttonLeft.drawButton(this.minecraft, mouseX, mouseY);
            }

            if (this.buttonRight != null) {
                this.buttonRight.yPosition = y;
                this.buttonRight.drawButton(this.minecraft, mouseX, mouseY);
            }
        }

        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonLeft.mousePressed(this.minecraft, x, y)) {
                if (this.buttonLeft instanceof GuiOptionButton) {
                    this.minecraft.gameSettings.setOptionValue(((GuiOptionButton) this.buttonLeft).returnEnumOptions(), 1);
                    this.buttonLeft.displayString = this.minecraft.gameSettings.getKeyBinding(EnumOptions.getEnumOptions(this.buttonLeft.id));
                }

                return true;
            } else if (this.buttonRight != null && this.buttonRight.mousePressed(this.minecraft, x, y)) {
                if (this.buttonRight instanceof GuiOptionButton) {
                    this.minecraft.gameSettings.setOptionValue(((GuiOptionButton) this.buttonRight).returnEnumOptions(), 1);
                    this.buttonRight.displayString = this.minecraft.gameSettings.getKeyBinding(EnumOptions.getEnumOptions(this.buttonRight.id));
                }

                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonLeft != null) {
                this.buttonLeft.mouseReleased(x, y);
            }

            if (this.buttonRight != null) {
                this.buttonRight.mouseReleased(x, y);
            }
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }
    }
}
