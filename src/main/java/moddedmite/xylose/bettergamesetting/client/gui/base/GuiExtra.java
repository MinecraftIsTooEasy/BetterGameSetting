package moddedmite.xylose.bettergamesetting.client.gui.base;

import net.minecraft.Gui;
import net.minecraft.Tessellator;

public class GuiExtra extends Gui {
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f4 = 1.0F / textureWidth;
        float f5 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * f4), (double) ((v + (float) height) * f5));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + (float) width) * f4), (double) ((v + (float) height) * f5));
        tessellator.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + (float) width) * f4), (double) (v * f5));
        tessellator.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * f4), (double) (v * f5));
        tessellator.draw();
    }

    public static void func_152125_a(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f4 = 1.0F / tileWidth;
        float f5 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * f4), (double) ((v + (float) vHeight) * f5));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + (float) uWidth) * f4), (double) ((v + (float) vHeight) * f5));
        tessellator.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + (float) uWidth) * f4), (double) (v * f5));
        tessellator.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * f4), (double) (v * f5));
        tessellator.draw();
    }
}
