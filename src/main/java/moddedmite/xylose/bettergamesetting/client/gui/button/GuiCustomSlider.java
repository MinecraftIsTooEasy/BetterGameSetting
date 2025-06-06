package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.EnumOptions;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiCustomSlider extends GuiButton {
    public float sliderValue = 1.0f;
    public boolean dragging;
    private EnumOptions idFloat;

    public GuiCustomSlider(int id, int xPos, int yPos, int width, int height, EnumOptions par4EnumOptions, String displayString, float sliderValue) {
        super(id, xPos, yPos, width, height, displayString);
        this.idFloat = par4EnumOptions;
        this.sliderValue = sliderValue;
    }

    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (!this.enabled) {
            return;
        }
        if (this.drawButton) {
            if (this.dragging) {
                this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
                if (this.sliderValue < 0.0f) {
                    this.sliderValue = 0.0f;
                }
                if (this.sliderValue > 1.0f) {
                    this.sliderValue = 1.0f;
                }
                par1Minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
                this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0f) {
                this.sliderValue = 0.0f;
            }
            if (this.sliderValue > 1.0f) {
                this.sliderValue = 1.0f;
            }
            par1Minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
            this.displayString = par1Minecraft.gameSettings.getKeyBinding(this.idFloat);
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }
}
