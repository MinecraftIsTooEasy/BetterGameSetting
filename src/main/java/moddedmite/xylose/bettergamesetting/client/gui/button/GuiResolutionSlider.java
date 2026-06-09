package moddedmite.xylose.bettergamesetting.client.gui.button;

import moddedmite.xylose.bettergamesetting.util.DisplayModeHelper;
import net.minecraft.EnumOptions;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static moddedmite.xylose.bettergamesetting.util.DisplayModeHelper.resolutions;

public class GuiResolutionSlider extends GuiOptionSlider {

    public GuiResolutionSlider(int buttonId, int x, int y) {
        super(buttonId, x, y, EnumOptions.RENDER_DISTANCE);
//        initResolutions();
        updateDisplayString();
    }


    private int getResolutionIndex(DisplayMode mode) {
        for (int i = 0; i < resolutions.size(); i++) {
            DisplayMode m = resolutions.get(i);
            if (m.getWidth() == mode.getWidth() && m.getHeight() == m.getHeight()) {
                return i;
            }
        }
        return 0;
    }

    private void updateDisplayString() {
        DisplayMode current = Display.getDisplayMode();
        this.sliderValue = getResolutionIndex(current) / (float) (resolutions.size() - 1);
        this.displayString = current.toString();
    }

    @Override
    protected void mouseDragged(Minecraft client, int mouseX, int mouseY) {
        if (this.enabled && client.gameSettings.fullScreen) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = Math.max(0.0F, Math.min(1.0F, this.sliderValue));

                int index = (int) (this.sliderValue * (resolutions.size() - 1));
                DisplayMode selected = resolutions.get(index);
                this.displayString = selected.toString();
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(
                    this.xPosition + (int) (this.sliderValue * (this.width - 8)),
                    this.yPosition,
                    0, 66, 4, 20
            );
            this.drawTexturedModalRect(
                    this.xPosition + (int) (this.sliderValue * (this.width - 8)) + 4,
                    this.yPosition,
                    196, 66, 4, 20
            );
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            this.sliderValue = Math.max(0.0F, Math.min(1.0F, this.sliderValue));
            return true;
        }
        return false;
    }

    private void applyResolution(DisplayMode mode) throws LWJGLException {
        try {
            Display.setDisplayMode(mode);
            if (Minecraft.getMinecraft().isFullScreen()) {
                Display.setFullscreen(true);
            } else {
                Minecraft.getMinecraft().resize(mode.getWidth(), mode.getHeight());
            }
            updateDisplayString();
        } catch (Exception e) {
            e.printStackTrace();
            Display.setDisplayMode(Display.getDesktopDisplayMode());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
        Minecraft minecraft = Minecraft.getMinecraft();
        int index = (int) (this.sliderValue * (resolutions.size() - 1));
        try {
            applyResolution(resolutions.get(index));
            ScaledResolution var3 = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
            int var4 = var3.getScaledWidth();
            int var5 = var3.getScaledHeight();
            minecraft.currentScreen.setWorldAndResolution(minecraft, var4, var5);
            minecraft.gameSettings.saveOptions();
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
}