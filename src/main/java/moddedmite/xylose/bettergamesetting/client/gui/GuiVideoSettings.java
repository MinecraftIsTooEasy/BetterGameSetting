package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiOptionsRowList;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import moddedmite.xylose.bettergamesetting.util.OpenGlHelperExtra;
import net.minecraft.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GuiVideoSettings extends GuiScreen {
    public GuiScreen parentGuiScreen;
    protected String screenTitle = "Video Settings";
    private GameSettings guiGameSettings;
    private GuiOptionsRowList optionsRowList;
    /**
     * An array of all of EnumOptions's video options.
     */
    private static final EnumOptions[] videoOptions = new EnumOptions[]{EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.GAMMA, EnumOptions.RENDER_CLOUDS, EnumOptions.PARTICLES, EnumOptions.USE_FULLSCREEN, EnumOptions.ENABLE_VSYNC
//            ,EnumOptions.MIPMAP_LEVELS, EnumOptions.ANISOTROPIC_FILTERING
    };

    public GuiVideoSettings(GuiScreen guiScreen, GameSettings gameSettings) {
        this.parentGuiScreen = guiScreen;
        this.guiGameSettings = gameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.screenTitle = I18n.getString("options.videoTitle");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.getString("gui.done")));

//        if (OpenGlHelperExtra.isNvidiaGL) {
        this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, videoOptions);
//        } else {
//            EnumOptions[] aoptions = new EnumOptions[videoOptions.length - 1];
//            int i = 0;
//            EnumOptions[] aoptions1 = videoOptions;
//            int j = aoptions1.length;
//
//            for (int k = 0; k < j; ++k) {
//                EnumOptions options = aoptions1[k];
//
//                if (options != EnumOptions.ADVANCED_OPENGL ) {
//                    aoptions[i] = options;
//                    ++i;
//                }
//            }
//             this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25, aoptions);
//        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int l = this.guiGameSettings.guiScale;
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.guiGameSettings.guiScale != l) {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, i1, j1);
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
     * mouseMove, which==0 or which==1 is mouseUp
     */
    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        int l = this.guiGameSettings.guiScale;
        super.mouseMovedOrUp(mouseX, mouseY, state);
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);

        if (this.guiGameSettings.guiScale != l) {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i1 = scaledresolution.getScaledWidth();
            int j1 = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, i1, j1);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 5, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
