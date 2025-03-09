package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.EnumOptions;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiOptionSlider extends GuiButton {
    private float sliderValue;
    public boolean dragging;
    private EnumOptions options;
    private final float minValue;
    private final float maxValue;

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn) {
        this(buttonId, x, y, optionIn, 0.0F, 1.0F);
    }

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn, float minValueIn, float maxValueIn) {
        super(buttonId, x, y, 150, 20, "");
        this.sliderValue = 1.0F;
        this.options = optionIn;
        this.minValue = minValueIn;
        this.maxValue = maxValueIn;
        Minecraft minecraft = Minecraft.getMinecraft();
        this.sliderValue = this.options.normalizeValue(minecraft.gameSettings.getOptionFloatValue(optionIn), options);
        this.displayString = minecraft.gameSettings.getKeyBinding(optionIn);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    public int getHoverState(boolean mouseOver) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

                if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
                }

                float f = this.options.denormalizeValue(this.sliderValue, this.options);
                mc.gameSettings.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f, this.options);
                this.displayString = mc.gameSettings.getKeyBinding(this.options);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

            if (this.sliderValue < 0.0F) {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F) {
                this.sliderValue = 1.0F;
            }

            mc.gameSettings.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue, this.options));
            this.displayString = mc.gameSettings.getKeyBinding(this.options);
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}
