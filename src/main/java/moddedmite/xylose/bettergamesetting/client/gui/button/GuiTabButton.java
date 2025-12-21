package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.FontRenderer;
import net.minecraft.Minecraft;

public class GuiTabButton extends GuiOptionButton {
    private boolean isSelected;

    public GuiTabButton(int buttonId, int x, int y, int buttonLength, int buttonWidth, String displayString) {
        super(buttonId, x, y, displayString);
        this.isSelected = false;
    }

    public GuiTabButton(int buttonId, int x, int y, String displayString, boolean isSelected) {
        super(buttonId, x, y, displayString);
        this.isSelected = isSelected;
    }
    public GuiTabButton(int buttonId, int x, int y, int buttonLength, int buttonWidth, String displayString, boolean isSelected) {
        super(buttonId, x, y, displayString);
        this.isSelected = isSelected;
    }

    @Override
    public void drawButton(Minecraft client, int mouseX, int mouseY) {
        if (this.drawButton) {
            FontRenderer textRenderer = client.fontRenderer;
            this.field_82253_i = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                               mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            // 绘制按钮背景
            int backgroundColor = this.isSelected ? 0x77FFFFFF : 0x33FFFFFF;
            drawRect(this.xPosition, this.yPosition,
                     this.xPosition + this.width, this.yPosition + this.height,
                     backgroundColor);

            // 绘制边框线（使用白线）
            int borderColor = 0xFFFFFFFF; // 白色
            drawHorizontalLine(this.xPosition, this.xPosition + this.width - 1, this.yPosition, borderColor); // 顶部线
            drawHorizontalLine(this.xPosition, this.xPosition + this.width - 1, this.yPosition + this.height - 1, borderColor); // 底部线
            drawVerticalLine(this.xPosition, this.yPosition, this.yPosition + this.height - 1, borderColor); // 左边线
            drawVerticalLine(this.xPosition + this.width - 1, this.yPosition, this.yPosition + this.height - 1, borderColor); // 右边线

            this.mouseDragged(client, mouseX, mouseY);

            // 设置文本颜色
            int textColor = 0xFFFFFF; // 默认白色文字
            if (!this.enabled) {
                textColor = 0xA0A0A0; // 禁用状态灰色文字
            } else if (this.field_82253_i) {
                textColor = 0xFFFFA0; // 悬停状态淡黄色文字
            }

            this.drawCenteredString(textRenderer, this.displayString,
                                  this.xPosition + this.width / 2,
                                  this.yPosition + (this.height - 8) / 2,
                                  textColor);
        }
    }

    /**
     * 绘制水平线
     */
    protected void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int temp = startX;
            startX = endX;
            endX = temp;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * 绘制垂直线
     */
    protected void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int temp = startY;
            startY = endY;
            endY = temp;
        }
        drawRect(x, startY, x + 1, endY + 1, color);
    }

    /**
     * 设置选中状态
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    /**
     * 获取选中状态
     */
    public boolean isSelected() {
        return this.isSelected;
    }
}
