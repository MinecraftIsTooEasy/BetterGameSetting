package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import net.minecraft.*;

public class GuiYesNoResetKeyBinding extends GuiScreen {
    protected GuiScreen parentScreen;
    protected String buttonText1;
    protected String buttonText2;

    public GuiYesNoResetKeyBinding(GuiScreen guiScreen) {
        this.parentScreen = guiScreen;
        this.buttonText1 = I18n.getString("gui.yes");
        this.buttonText2 = I18n.getString("gui.no");
    }

    public void initGui() {
        this.buttonList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 6 + 96, this.buttonText1));
        this.buttonList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.buttonText2));
    }

    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 0) {
            for (KeyBinding keybinding : this.mc.gameSettings.keyBindings) {
                ((IGameSetting) this.mc.gameSettings).setOptionKeyBinding(keybinding, KeyBindingExtra.getKeyCodeDefault(keybinding.keyDescription));
            }
            KeyBinding.resetKeyBindingArrayAndHash();
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if (guiButton.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    protected void keyTyped(char par1, int par2) {
        if (par2 == 1) {
            this.actionPerformed((GuiButton) this.buttonList.get(1));
        } else {
            super.keyTyped(par1, par2);
        }
    }

    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, I18n.getString("controls.reset_keybinding_info_0"), this.width / 2, 70, 16777215);
        this.drawCenteredString(this.fontRenderer, I18n.getString("controls.reset_keybinding_info_1"), this.width / 2, 90, 16777215);
        super.drawScreen(i, j, f);
    }
}
