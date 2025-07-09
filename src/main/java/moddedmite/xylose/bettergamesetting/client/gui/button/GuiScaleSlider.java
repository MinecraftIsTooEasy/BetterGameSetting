package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class GuiScaleSlider extends GuiOptionSlider {
    public int sliderValue;
    public boolean dragging;
    public final EnumOptions options;
    private final int minValue;
    private final int maxValue;
    private Minecraft client;

    public GuiScaleSlider(int buttonId, int x, int y, EnumOptions optionIn, int minValueIn, int maxValue) {
        super(buttonId, x, y, optionIn);
        this.options = optionIn;
        this.minValue = minValueIn;
        this.maxValue = maxValue;
        client = Minecraft.getMinecraft();
        this.sliderValue = MathHelper.clamp_int(client.gameSettings.guiScale, minValueIn, maxValue);
        this.displayString = getDisplayString(client);
    }

    @Override
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft client, int mouseX, int mouseY) {
        if (this.enabled) {
            if (this.dragging) {
                final float index = (mouseX - (this.xPosition + 4)) / (float) (this.width - 10);
                sliderValue = Math.round(client.gameSettings.guiScale > maxValue ? client.gameSettings.guiScale * index : maxValue * index);
                this.sliderValue = MathHelper.clamp_int(this.sliderValue, this.minValue, this.maxValue);
                this.displayString = getDisplayString(client);
            }

            client.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            int renderX = Math.round(this.xPosition + (sliderValue * ((this.width) / maxValue)));
            renderX = Math.max(this.xPosition, renderX);
            renderX = Math.min(this.xPosition + width - 8, renderX);
            this.drawTexturedModalRect(renderX, this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(renderX + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public String getDisplayString(Minecraft client) {
        String ret = I18n.getString("options.guiScale") + ": ";
        if (sliderValue == 0) {
            return ret + I18n.getString("options.guiScale.auto");
        } else {
            return ret + sliderValue + "x";
        }
    }

    @Override
    public boolean mousePressed(Minecraft client, int mouseX, int mouseY) {
        if (super.mousePressed(client, mouseX, mouseY)) {
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
        if (sliderValue != this.client.gameSettings.guiScale) {
            this.client.gameSettings.guiScale = this.sliderValue;
            ScaledResolution resolution = new ScaledResolution(this.client.gameSettings, this.client.displayWidth, this.client.displayHeight);
            int scaledWidth = resolution.getScaledWidth();
            int scaledHeight = resolution.getScaledHeight();
            this.client.currentScreen.setWorldAndResolution(this.client, scaledWidth, scaledHeight);
        }
    }
}

