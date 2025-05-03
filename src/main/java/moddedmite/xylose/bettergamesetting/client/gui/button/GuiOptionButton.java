package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.*;

import java.util.ArrayList;
import java.util.List;

public class GuiOptionButton extends GuiButton {
    private final EnumOptions enumOptions;

    public GuiOptionButton(int buttonId, int x, int y, String displayString) {
        this(buttonId, x, y, (EnumOptions) null, displayString);
    }

    public GuiOptionButton(int buttonId, int x, int y, int buttonLength, int buttonWidth, String displayString) {
        super(buttonId, x, y, buttonLength, buttonWidth, displayString);
        this.enumOptions = null;
    }

    public GuiOptionButton(int buttonId, int x, int y, EnumOptions optionIn, String displayString) {
        super(buttonId, x, y, 150, 20, displayString);
        this.enumOptions = optionIn;
    }

    public EnumOptions returnEnumOptions() {
        return this.enumOptions;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseX <= this.xPosition + this.width && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height;
    }

    public void drawTooltip(String text, int mouseX, int mouseY) {
        String transText = I18n.getString(text);
        if (transText == null) return;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();

        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int maxWidth = screenWidth - mouseX - 20;
        int lineHeight = fr.FONT_HEIGHT + 2;

        List<String> lines = new ArrayList<>();
        for (String rawLine : transText.split("\n")) {
            lines.addAll(fr.listFormattedStringToWidth(rawLine, Math.min(maxWidth, 300)));
        }

        int tooltipWidth = lines.stream().mapToInt(fr::getStringWidth).max().orElse(0);
        int tooltipHeight = lines.size() * lineHeight;

        int adjustedX = mouseX + 12;
        int adjustedY = mouseY - 10;

        if (adjustedX + tooltipWidth + 6 > screenWidth) {
            adjustedX = screenWidth - tooltipWidth - 6 - 5;
        }

        if (adjustedY + tooltipHeight > screenHeight) {
            adjustedY = screenHeight - tooltipHeight - 5;
        }

        drawGradientRect(
                adjustedX - 3, adjustedY - 3,
                adjustedX + tooltipWidth + 3, adjustedY + tooltipHeight + 3,
                0xAA000000, 0xAA000000
        );

        int yPos = adjustedY;
        for (String line : lines) {
            fr.drawStringWithShadow(line, adjustedX, yPos, 0xFFFFFF);
            yPos += lineHeight;
        }
    }
}
