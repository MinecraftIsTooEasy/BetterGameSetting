package moddedmite.xylose.bettergamesetting.util;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ScreenUtil extends Gui {
    public static ScreenUtil instance;
    private final Minecraft client;

    public ScreenUtil() {
        this.client = Minecraft.getMinecraft();
    }

    public static ScreenUtil getInstance() {
        return instance == null ? instance = new ScreenUtil() : instance;
    }

    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f4 = 1.0F / textureWidth;
        float f5 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, (y + height), 0.0D, (u * f4), ((v + (float) height) * f5));
        tessellator.addVertexWithUV((x + width), (y + height), 0.0D, ((u + (float) width) * f4), ((v + (float) height) * f5));
        tessellator.addVertexWithUV((x + width), y, 0.0D, ((u + (float) width) * f4), (v * f5));
        tessellator.addVertexWithUV(x, y, 0.0D, (u * f4), (v * f5));
        tessellator.draw();
    }

    public static void func_152125_a(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f4 = 1.0F / tileWidth;
        float f5 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, (y + height), 0.0D, (u * f4), ((v + (float) vHeight) * f5));
        tessellator.addVertexWithUV((x + width), (y + height), 0.0D, ((u + (float) uWidth) * f4), ((v + (float) vHeight) * f5));
        tessellator.addVertexWithUV((x + width), y, 0.0D, ((u + (float) uWidth) * f4), (v * f5));
        tessellator.addVertexWithUV(x, y, 0.0D, (u * f4), (v * f5));
        tessellator.draw();
    }

    public void drawTooltipTranslated(String text, int x, int y, Object... objects) {
        this.drawTooltip(List.of(I18n.getStringParams(text, objects)), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawTooltipTranslated(String text, int x, int y) {
        this.drawTooltip(List.of(I18n.getString(text)), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawTooltip(String text, int x, int y) {
        this.drawTooltip(List.of(text), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawTooltip(List<String> textLines, int x, int y) {
        this.drawTooltip(textLines, x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawTooltip(List<String> textLines, int x, int y, FontRenderer font, int width, int height) {
        if (!textLines.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            List<String> wrappedLines = new ArrayList<>();
            int maxWidth = 0;

            for (String textLine : textLines) {
                int lineWidth = font.getStringWidth(textLine);

                if (lineWidth > 160) {
                    List<String> wrapped = wrapText(textLine, font, 160);
                    wrappedLines.addAll(wrapped);

                    for (String wrappedLine : wrapped) {
                        int wrappedWidth = font.getStringWidth(wrappedLine);
                        if (wrappedWidth > maxWidth) {
                            maxWidth = wrappedWidth;
                        }
                    }
                } else {
                    wrappedLines.add(textLine);
                    if (lineWidth > maxWidth) {
                        maxWidth = lineWidth;
                    }
                }
            }

            textLines = wrappedLines;

            int tooltipX = x + 12;
            int tooltipY = y - 20;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += 2 + (textLines.size() - 1) * 10;
            }

            if (tooltipX + maxWidth > width) {
                tooltipX = x - maxWidth - 12;
            }

            if (tooltipX < 4) {
                tooltipX = 4;
            }

            if (tooltipY + tooltipHeight + 6 > height) {
                tooltipY = height - tooltipHeight - 6;
            }

            if (tooltipY < 4) {
                tooltipY = 4;
            }

            this.zLevel = 300.0F;
            int backgroundColor = -267386864;
            this.drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + maxWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX + maxWidth + 3, tooltipY - 3, tooltipX + maxWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            int borderColorStart = 1347420415;
            int borderColorEnd = (borderColorStart & 16711422) >> 1 | borderColorStart & -16777216;
            this.drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            this.drawGradientRect(tooltipX + maxWidth + 2, tooltipY - 3 + 1, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            for (int i = 0; i < textLines.size(); ++i) {
                String line = textLines.get(i);
                font.drawStringWithShadow(line, tooltipX, tooltipY, -1);

                if (i == 0) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            this.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void drawButtonTooltipTranslated(String text, int x, int y, Object... objects) {
        this.drawButtonTooltip(List.of(I18n.getStringParams(text, objects)), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawButtonTooltipTranslated(String text, int x, int y) {
        this.drawButtonTooltip(List.of(I18n.getString(text)), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawButtonTooltip(String text, int x, int y) {
        this.drawButtonTooltip(List.of(text), x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawButtonTooltip(List<String> textLines, int x, int y) {
        this.drawButtonTooltip(textLines, x, y, this.client.fontRenderer, this.client.currentScreen.width, this.client.currentScreen.height);
    }

    public void drawButtonTooltip(List<String> textLines, int x, int y, FontRenderer font, int width, int height) {
        if (!textLines.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            List<String> wrappedLines = new ArrayList<>();
            int maxWidth = 0;

            for (String textLine : textLines) {
                int lineWidth = font.getStringWidth(textLine);

                if (lineWidth > 160) {
                    List<String> wrapped = wrapText(textLine, font, 160);
                    wrappedLines.addAll(wrapped);

                    for (String wrappedLine : wrapped) {
                        int wrappedWidth = font.getStringWidth(wrappedLine);
                        if (wrappedWidth > maxWidth) {
                            maxWidth = wrappedWidth;
                        }
                    }
                } else {
                    wrappedLines.add(textLine);
                    if (lineWidth > maxWidth) {
                        maxWidth = lineWidth;
                    }
                }
            }

            textLines = wrappedLines;

            int tooltipHeight = 8;
            if (textLines.size() > 1) {
                tooltipHeight += 2 + (textLines.size() - 1) * 10;
            } else {
                tooltipHeight += 2;
            }

            int tooltipY;
            int verticalOffset = 20;

            int belowY = y + verticalOffset;
            int aboveY = y - tooltipHeight - verticalOffset;

            if (y < height / 2) {
                tooltipY = Math.min(belowY, height - tooltipHeight - 4);
            } else {
                tooltipY = Math.max(aboveY, 4);
            }

            tooltipY = Math.max(4, Math.min(tooltipY, height - tooltipHeight - 4));

            int tooltipX = x + 12;

            if (tooltipX + maxWidth > width) {
                tooltipX = x - maxWidth - 12;
            }

            if (tooltipX < 4) {
                tooltipX = 4;
            }

            this.zLevel = 300.0F;
            int backgroundColor = -267386864;
            this.drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + maxWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            this.drawGradientRect(tooltipX + maxWidth + 3, tooltipY - 3, tooltipX + maxWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            int borderColorStart = 1347420415;
            int borderColorEnd = (borderColorStart & 16711422) >> 1 | borderColorStart & -16777216;
            this.drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            this.drawGradientRect(tooltipX + maxWidth + 2, tooltipY - 3 + 1, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            this.drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + maxWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            this.drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + maxWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            int currentY = tooltipY;
            for (int i = 0; i < textLines.size(); ++i) {
                String line = textLines.get(i);
                font.drawStringWithShadow(line, tooltipX, currentY, -1);

                if (i == 0) {
                    currentY += 2;
                }

                currentY += 10;
            }

            this.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }


    private List<String> wrapText(String text, FontRenderer font, int maxWidth) {
        List<String> lines = new ArrayList<>();

        if (font.getStringWidth(text) <= maxWidth) {
            lines.add(text);
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = !currentLine.isEmpty() ? currentLine + " " + word : word;

            if (font.getStringWidth(testLine) <= maxWidth) {
                currentLine = new StringBuilder(testLine);
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                }
                if (font.getStringWidth(word) > maxWidth) {
                    List<String> splitWord = splitLongWord(word, font, maxWidth);
                    lines.addAll(splitWord);
                    currentLine = new StringBuilder();
                } else {
                    currentLine = new StringBuilder(word);
                }
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private List<String> splitLongWord(String word, FontRenderer font, int maxWidth) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();

        for (char c : word.toCharArray()) {
            String testPart = currentPart.toString() + c;
            if (font.getStringWidth(testPart) <= maxWidth) {
                currentPart.append(c);
            } else {
                if (!currentPart.isEmpty()) {
                    parts.add(currentPart.toString());
                }
                currentPart = new StringBuilder(String.valueOf(c));
            }
        }

        if (!currentPart.isEmpty()) {
            parts.add(currentPart.toString());
        }

        return parts;
    }

    public GuiButton getButtonById(int id, List<GuiButton> buttons) {
        for (GuiButton button : buttons) {
            if (button.id == id) {
                return button;
            }
        }
        return null;
    }

    public static void renderScrollingString(FontRenderer font, String text, int left, int top, int right, int bottom, int color) {
        renderScrollingString(font, text, (left + right) / 2, left, top, right, bottom, color);
    }

    public static void renderScrollingString(FontRenderer font, String text, int centerX, int left, int top, int right, int bottom, int color) {
        int textWidth = font.getStringWidth(text);
        int centerY = (top + bottom - 9) / 2 + 1;
        int availableWidth = right - left;

        if (textWidth > availableWidth) {
            int overflow = textWidth - availableWidth;
            double timeSeconds = (double) System.currentTimeMillis() / 1000.0;
            double cycleDuration = Math.max((double) overflow * 0.5, 3.0);
            double interpolationFactor = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * timeSeconds / cycleDuration)) / 2.0 + 0.5;
            double scrollOffset = Mth.lerp(interpolationFactor, 0.0F, overflow);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            Minecraft mc = Minecraft.getMinecraft();
            int scale = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaleFactor();
            GL11.glScissor(left * scale, mc.displayHeight - bottom * scale, (right - left) * scale, (bottom - top) * scale);
            font.drawStringWithShadow(text, left - (int) scrollOffset, centerY, color);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        } else {
            int clampedX = Mth.clamp(centerX, left + textWidth / 2, right - textWidth / 2);
            font.drawStringWithShadow(text, clampedX - textWidth / 2, centerY, color);
        }
    }
}
