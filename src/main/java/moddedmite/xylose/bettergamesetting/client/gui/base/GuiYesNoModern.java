package moddedmite.xylose.bettergamesetting.client.gui.base;

import moddedmite.xylose.bettergamesetting.api.GuiYesNoCallback;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;

import java.util.List;

public class GuiYesNoModern extends GuiScreen {
    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    protected GuiYesNoCallback parentScreen;
    protected String messageLine1;
    private String messageLine2;
    /**
     * The text shown for the first button in GuiYesNo
     */
    protected String confirmButtonText;
    /**
     * The text shown for the second button in GuiYesNo
     */
    protected String cancelButtonText;
    protected int parentScreenId;
    private int disableTimer;

    public GuiYesNoModern(GuiYesNoCallback parentScreen, String messageLine, int parentScreenId) {
        this(parentScreen, messageLine, "", parentScreenId);
    }

    public GuiYesNoModern(GuiYesNoCallback parentScreen, String messageLine1, String messageLine2, int parentScreenId) {
        this.parentScreen = parentScreen;
        this.messageLine1 = messageLine1;
        this.messageLine2 = messageLine2;
        this.parentScreenId = parentScreenId;
        this.confirmButtonText = I18n.getString("gui.yes");
        this.cancelButtonText = I18n.getString("gui.no");
    }

    public GuiYesNoModern(GuiYesNoCallback parentScreen, String messageLine1, String messageLine2, String confirmButtonText, String cancelButtonText, int parentScreenId) {
        this.parentScreen = parentScreen;
        this.messageLine1 = messageLine1;
        this.messageLine2 = messageLine2;
        this.confirmButtonText = confirmButtonText;
        this.cancelButtonText = cancelButtonText;
        this.parentScreenId = parentScreenId;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.confirmButtonText));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.cancelButtonText));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        this.parentScreen.confirmClicked(button.id == 0, this.parentScreenId);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.messageLine1, this.width / 2, 70, 16777215);
        this.drawCenteredString(this.fontRenderer, this.messageLine2, this.width / 2, 90, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void setDisableTimer(int disableTimer) {
        this.disableTimer = disableTimer;

        for (GuiButton button : (List<GuiButton>) this.buttonList) {
            button.enabled = false;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();

        if (--this.disableTimer == 0) {
            for (GuiButton button : (List<GuiButton>) this.buttonList) {
                button.enabled = true;
            }
        }
    }
}
