package moddedmite.xylose.bettergamesetting.client.gui.button;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class GuiOptionSlider extends GuiButton {
    float sliderValue;
    public boolean dragging;
    public EnumOptions options;
    private final float minValue;
    private final float maxValue;
    private Minecraft client;
    private boolean realtimeUpdate;

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn) {
        this(buttonId, x, y, optionIn, 0.0F, 1.0F, false);
    }

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn, boolean realtimeUpdate) {
        this(buttonId, x, y, optionIn, 0.0F, 1.0F, realtimeUpdate);
    }

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn, float minValueIn, float maxValueIn) {
        this(buttonId, x, y, optionIn, minValueIn, maxValueIn, true);
    }

    public GuiOptionSlider(int buttonId, int x, int y, EnumOptions optionIn, float minValueIn, float maxValueIn, boolean realtimeUpdate) {
        super(buttonId, x, y, 150, 20, "");
        this.sliderValue = 1.0F;
        this.options = optionIn;
        this.minValue = minValueIn;
        this.maxValue = maxValueIn;
        this.realtimeUpdate = realtimeUpdate;
        this.client = Minecraft.getMinecraft();
        this.sliderValue = this.options.normalizeValue(client.gameSettings.getOptionFloatValue(optionIn), options);
        this.displayString = client.gameSettings.getKeyBinding(optionIn);
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
    protected void mouseDragged(Minecraft client, int mouseX, int mouseY) {
        if (this.enabled) {
            if (this.dragging) {
                float newValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);

                if (newValue < 0.0F) {
                    newValue = 0.0F;
                }
                if (newValue > 1.0F) {
                    newValue = 1.0F;
                }

                this.sliderValue = newValue;
                float displayValue = this.options.denormalizeValue(this.sliderValue, this.options);
                
                if (realtimeUpdate) {
                    client.gameSettings.setOptionFloatValue(this.options, displayValue);
                    this.displayString = client.gameSettings.getKeyBinding(this.options);
                } else {
                    float origin = client.gameSettings.getOptionFloatValue(this.options);
                    client.gameSettings.setOptionFloatValue(this.options, displayValue);
                    this.displayString = client.gameSettings.getKeyBinding(this.options);
                    client.gameSettings.setOptionFloatValue(this.options, origin);
                }
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

            if (realtimeUpdate) {
                float displayValue = this.options.denormalizeValue(this.sliderValue, this.options);
                mc.gameSettings.setOptionFloatValue(this.options, displayValue);
                this.displayString = mc.gameSettings.getKeyBinding(this.options);
            } else {
                float displayValue = this.options.denormalizeValue(this.sliderValue, this.options);
                float originalGameSettingValue = mc.gameSettings.getOptionFloatValue(this.options);
                mc.gameSettings.setOptionFloatValue(this.options, displayValue);
                this.displayString = mc.gameSettings.getKeyBinding(this.options);
                mc.gameSettings.setOptionFloatValue(this.options, originalGameSettingValue);
            }
            
            this.dragging = true;
            return true;
        }
        return false;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
        if (this.dragging) {
            float newValue = this.options.denormalizeValue(this.sliderValue, this.options);
            this.client.gameSettings.setOptionFloatValue(this.options, newValue);

            this.displayString = this.client.gameSettings.getKeyBinding(this.options);
        }
        this.dragging = false;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseX <= this.xPosition + this.width && mouseY >= this.yPosition && mouseY <= this.yPosition + this.height;
    }
}