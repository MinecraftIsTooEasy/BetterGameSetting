package moddedmite.xylose.bettergamesetting.client.gui.world;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.EnumChatFormatting;
import net.minecraft.FontRenderer;
import net.minecraft.I18n;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import net.minecraft.SaveFormatComparator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
    private static final int TEXT_OFFSET = 36;
    private static final int RIGHT_PADDING = 2;
    private static final String ELLIPSIS = "...";

    private final SaveFormatComparator save;
    private final String[] fullTexts = new String[3];
    private static final int[] LINE_OFFSETS = {1, 12, 22};
    private static final int[] LINE_COLORS = {16777215, 8421504, 8421504};

    public GuiListWorldSelectionEntry(int slotIndex, SaveFormatComparator comparator) {
        this.save = comparator;
        String name = comparator.getDisplayName();
        if (name == null || MathHelper.stringNullOrLengthZero(name)) {
            name = I18n.getString("selectWorld.world") + " " + (slotIndex + 1);
        }
        if (!comparator.passed_validation) {
            name = name + EnumChatFormatting.DARK_GRAY + " | " + EnumChatFormatting.RED + (comparator.failed_validation_reason == null ? "INVALID WORLD" : comparator.failed_validation_reason);
        }
        this.fullTexts[0] = name;
        this.fullTexts[1] = comparator.getFileName() + " (" + DATE_FORMAT.format(new Date(comparator.getLastTimePlayed())) + ")";
        String mode;
        if (comparator.requiresConversion()) {
            mode = I18n.getString("selectWorld.conversion") + " ";
        } else {
            mode = getLocalizedGameMode(comparator.getEnumGameType().getID());
            if (comparator.isHardcoreModeEnabled()) {
                mode = EnumChatFormatting.DARK_RED + I18n.getString("gameMode.hardcore") + EnumChatFormatting.RESET;
            }
            if (comparator.areSkillsEnabled()) {
                mode = mode + ", " + I18n.getString("selectWorld.skills");
            }
            if (comparator.getCheatsEnabled()) {
                mode = mode + ", " + I18n.getString("selectWorld.cheats");
            }
        }
        this.fullTexts[2] = mode;
    }

    private static String getLocalizedGameMode(int id) {
        return switch (id) {
            case 0 -> I18n.getString("gameMode.survival");
            case 1 -> I18n.getString("gameMode.creative");
            case 2 -> I18n.getString("gameMode.adventure");
            default -> "";
        };
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        GuiListWorldSelection.drawIcon(this.save.getFileName(), x, y);
        Minecraft client = Minecraft.getMinecraft();
        for (int i = 0; i < this.fullTexts.length; ++i) {
            client.fontRenderer.drawStringWithShadow(trimToWidth(client.fontRenderer, this.fullTexts[i], getTextWidth(listWidth)), x + TEXT_OFFSET, y + LINE_OFFSETS[i], LINE_COLORS[i]);
        }
    }

    public void drawHoverTooltip(int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        for (int i = 0; i < this.fullTexts.length; ++i) {
            String text = this.fullTexts[i];
            if (font.getStringWidth(text) <= getTextWidth(listWidth)) continue;
            int lineY = y + LINE_OFFSETS[i];
            if (mouseY >= lineY - 1 && mouseY < lineY + font.FONT_HEIGHT + 1 && mouseX >= x + TEXT_OFFSET && mouseX < x + listWidth) {
                ScreenUtil.getInstance().drawTooltip(Collections.singletonList(text), mouseX, mouseY);
                return;
            }
        }
    }

    private static int getTextWidth(int listWidth) {
        return listWidth - TEXT_OFFSET - RIGHT_PADDING;
    }

    private static String trimToWidth(FontRenderer font, String text, int maxWidth) {
        if (font.getStringWidth(text) <= maxWidth) return text;
        int width = maxWidth - font.getStringWidth(ELLIPSIS);
        if (width <= 0) return ELLIPSIS;
        return font.trimStringToWidth(text, width) + ELLIPSIS;
    }

    @Override
    public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    @Override
    public void keyTyped(int slotIndex, char typedChar, int keyCode) {
    }

    @Override
    public void setSelected(int slotIndex, int mouseX, int mouseY) {
    }
}
