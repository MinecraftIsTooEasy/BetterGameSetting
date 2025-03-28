package moddedmite.xylose.bettergamesetting.client.gui.button;

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

public class GuiResolutionSlider extends GuiOptionSlider {
    private static List<DisplayMode> resolutions = new ArrayList<>();
    private static boolean resolutionsInitialized = false;

    public GuiResolutionSlider(int buttonId, int x, int y) throws LWJGLException {
        super(buttonId, x, y, EnumOptions.RENDER_DISTANCE); // 使用特定选项标识
        initResolutions();
        updateDisplayString();
    }

    private void initResolutions() throws LWJGLException {
        if (!resolutionsInitialized) {
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            for (DisplayMode mode : modes) {
                if (!containsResolution(mode)) {
                    resolutions.add(mode);
                }
            }
            resolutions.sort((a, b) -> {
                if (a.getWidth() != b.getWidth()) return a.getWidth() - b.getWidth();
                return a.getHeight() - b.getHeight();
            });
            resolutionsInitialized = true;
        }
    }

    private boolean containsResolution(DisplayMode mode) {
        return resolutions.stream().anyMatch(m ->
                m.getWidth() == mode.getWidth() &&
                        m.getHeight() == mode.getHeight()
        );
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

    // 更新显示文字
    private void updateDisplayString() {
        DisplayMode current = Display.getDisplayMode();
        this.sliderValue = getResolutionIndex(current) / (float) (resolutions.size() - 1);
        this.displayString = current.getWidth() + "x" + current.getHeight();
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = Math.max(0.0F, Math.min(1.0F, this.sliderValue));

                int index = (int) (this.sliderValue * (resolutions.size() - 1));
                DisplayMode selected = resolutions.get(index);
                this.displayString = selected.getWidth() + "x" + selected.getHeight();
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

    // 应用分辨率
    private void applyResolution(DisplayMode mode) throws LWJGLException {
        try {
            Display.setDisplayMode(mode);
            if (Minecraft.getMinecraft().isFullScreen()) {
                Display.setFullscreen(true);
            } else {
//                Minecraft.getMinecraft().resizeWindow(mode.getWidth(), mode.getHeight());
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