package moddedmite.xylose.bettergamesetting.client.gui.base;

import moddedmite.xylose.bettergamesetting.client.gui.button.GuiTabButton;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GuiTabScreen extends GuiScreen {
    protected static final int TAB_WIDTH = 130;
    protected static final int TAB_HEIGHT = 24;
    protected static final int TAB_BUTTON_ID_BASE = 1001;
    protected int currentTab = TAB_BUTTON_ID_BASE;
    protected final List<GuiButton> tabButtons = new ArrayList<>();
    protected final Map<Integer, String> hoverTexts = new HashMap<>();

    protected abstract List<String> getTabNames();

    protected abstract void onTabChanged();

    protected void initHoverTexts() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        drawTabSeparatorLine();
        drawColoredLine(this.height - 35, this.width, 0xCC000000, 0x66ADB1B1);
        for (GuiButton tabButton : this.tabButtons) {
            drawTabButton(tabButton, this.mc, mouseX, mouseY);
        }
        drawTabContent(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawHoverText(mouseX, mouseY);
    }

    protected void drawTabContent(int mouseX, int mouseY, float partialTicks) {
    }

    protected void createTabButtons() {
        this.tabButtons.clear();
        List<String> names = getTabNames();
        int size = names.size();
        int width = Math.min(TAB_WIDTH, (this.width - 4) / Math.max(size, 1));
        int x = this.width / 2 - (width * size + 2) / 2;
        for (int i = 0; i < size; i++) {
            GuiButton button = new GuiTabButton(TAB_BUTTON_ID_BASE + i, x + i * width, 4, width, TAB_HEIGHT - 4, names.get(i));
            this.tabButtons.add(button);
            this.buttonList.add(button);
        }
    }

    protected boolean isTabButtonId(int id) {
        return id >= TAB_BUTTON_ID_BASE && id < TAB_BUTTON_ID_BASE + this.tabButtons.size();
    }

    protected void switchTab(int tabId) {
        this.currentTab = tabId;
        onTabChanged();
    }

    protected void drawTabButton(GuiButton button, Minecraft mc, int mouseX, int mouseY) {
        if (!button.drawButton) return;
        boolean isSelected = currentTab == button.id;
        int y = button.yPosition;
        int height = button.height;
        if (isSelected) {
            y -= 4;
            height += 4;
        }
        boolean isHovered = mouseX >= button.xPosition && mouseY >= y && mouseX < button.xPosition + button.width && mouseY < y + height;
        drawTabBorder(mc, button.xPosition, y, button.width, height, isSelected, isHovered, mc.fontRenderer.getStringWidth(button.displayString));
        drawCenteredString(mc.fontRenderer, button.displayString, button.xPosition + button.width / 2, y + (height - 8) / 2, 0xFFFFFF);
    }

    protected void drawTabBorder(Minecraft client, int x, int y, int width, int height, boolean isSelected, boolean isHovered, int fontWidth) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        final int WHITE = isHovered ? 0xFFFFFFFF : 0x66ADB1B1;
        final int BLACK = 0xCC000000;
        int bgColor = isSelected ? 0x00FFFFFFF : 0xDD000000;
        int heightAdjust = isSelected ? 1 : 2;
        if (client.gameSettings.isTransparentBackground()) drawRect(x + 2, y + 2, x + width - 2, y + height - 2, bgColor);
        drawRect(x, y, x + width, y + 1, BLACK);
        drawRect(x, y, x + 1, y + height - 2, BLACK);
        drawRect(x + width - 1, y, x + width, y + height - 2, BLACK);
        drawRect(x + 1, y + 1, x + width - 1, y + 2, WHITE);
        drawRect(x + 1, y + 2, x + 2, y + height - heightAdjust, WHITE);
        drawRect(x + width - 2, y + 2, x + width - 1, y + height - heightAdjust, WHITE);
        if (isHovered && !isSelected) drawRect(x + 2, y + height - 3, x + width - 2, y + height - 2, WHITE);
        if (isSelected) {
            int underlineY = y + height - 3;
            drawRect((x + width / 2) - fontWidth / 2, underlineY, ((x + width / 2) - fontWidth / 2) + fontWidth, underlineY + 1, 0xFFFFFFFF);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawTabSeparatorLine() {
        GuiButton selected = null;
        for (GuiButton button : this.tabButtons) {
            if (button.id == currentTab) {
                selected = button;
                break;
            }
        }
        if (selected != null) {
            if (selected.xPosition > 0) {
                drawRect(0, TAB_HEIGHT - 1, selected.xPosition + 2, TAB_HEIGHT, 0xCC000000);
                drawRect(0, TAB_HEIGHT - 2, selected.xPosition + 1, TAB_HEIGHT - 1, 0x66ADB1B1);
            }
            int rightStart = selected.xPosition + selected.width;
            if (rightStart < this.width) {
                drawRect(rightStart - 2, TAB_HEIGHT - 1, this.width, TAB_HEIGHT, 0xCC000000);
                drawRect(rightStart - 1, TAB_HEIGHT - 2, this.width, TAB_HEIGHT - 1, 0x66ADB1B1);
            }
        } else {
            drawColoredLine(TAB_HEIGHT - 2, this.width, 0x66ADB1B1, 0xCC000000);
        }
    }

    @SuppressWarnings("unchecked")
    protected void drawHoverText(int mouseX, int mouseY) {
        for (GuiButton button : (List<GuiButton>) this.buttonList) {
            if (!button.func_82252_a()) continue;
            if (isTabButtonId(button.id)) continue;
            List<String> hoverText = getHoverTextForButton(button);
            if (hoverText != null && !hoverText.isEmpty()) {
                ScreenUtil.getInstance().drawTooltip(hoverText, mouseX, mouseY);
                return;
            }
        }
    }

    protected List<String> getHoverTextForButton(GuiButton button) {
        String hoverText = hoverTexts.get(button.id);
        return hoverText == null || hoverText.isEmpty() ? null : List.of(hoverText);
    }

    protected void drawColoredLine(int y, int width, int topColor, int bottomColor) {
        drawRect(0, y, width, y + 1, topColor);
        drawRect(0, y + 1, width, y + 2, bottomColor);
    }
}
