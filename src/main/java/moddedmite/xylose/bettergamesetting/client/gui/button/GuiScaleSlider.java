package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class GuiScaleSlider extends GuiOptionSlider {
    public int sliderValue;
    public boolean dragging;
    public final EnumOptions options;
    private final int minValue;
    private final int maxValue;

    public GuiScaleSlider(int buttonId, int x, int y, EnumOptions optionIn, int minValueIn, int maxValue) {
        super(buttonId, x, y, optionIn);
        this.options = optionIn;
        this.minValue = minValueIn;
        this.maxValue = maxValue;
        Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = MathHelper.clamp_int(minecraft.gameSettings.guiScale, minValueIn, maxValue);
        this.displayString = getDisplayString(minecraft);
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled) {
            if (this.dragging) {
                final float index = (mouseX - (this.xPosition + 4)) / (float) (this.width - 10);
                sliderValue = Math.round(mc.gameSettings.guiScale > maxValue ? mc.gameSettings.guiScale * index : maxValue * index);
                this.sliderValue = MathHelper.clamp_int(this.sliderValue, this.minValue, this.maxValue);
                this.displayString = getDisplayString(mc);
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1, 1, 1, 1);

            int renderX = Math.round(this.xPosition + (sliderValue * ((this.width) / maxValue)));
            renderX = Math.max(this.xPosition, renderX);
            renderX = Math.min(this.xPosition + width - 8, renderX);
            this.drawTexturedModalRect(renderX, this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(renderX + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public String getDisplayString(Minecraft mc) {
        String ret = I18n.getString("options.guiScale") + ": ";
        if (sliderValue == 0) {
            return ret + I18n.getString("options.guiScale.auto");
        } else {
            return ret + sliderValue + "x";
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.gameSettings.guiScale = this.sliderValue;
        ScaledResolution var3 = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int var4 = var3.getScaledWidth();
        int var5 = var3.getScaledHeight();
        minecraft.currentScreen.setWorldAndResolution(minecraft, var4, var5);
    }
}

