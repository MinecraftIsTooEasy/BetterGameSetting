package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import org.lwjgl.input.Mouse;

public class GuiNewControls extends GuiScreen {
    private static final EnumOptions[] optionsArr = new EnumOptions[]{EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.TOUCHSCREEN};
    private GuiScreen parentScreen;
    protected String screenTitle = "Controls";
    private GameSettings options;
    public KeyBinding buttonId = null;
    public long time;
    private GuiKeyBindingList keyBindingList;
    private GuiButton buttonReset;
    private GuiSlider guiSlider;

    public GuiNewControls(GuiScreen screen, GameSettings settings) {
        this.parentScreen = screen;
        this.options = settings;
    }

    public void initGui() {
        this.keyBindingList = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.getString("gui.done")));
        this.buttonList.add(this.buttonReset = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.getString("controls.resetAll")));
        this.buttonList.add(new GuiButton(202, this.width / 2 - 155 + 160, 18 + 24, 150, 20, I18n.getString("gui.classicControls")));
        this.screenTitle = I18n.getString("controls.title");
        int i = 0;

        for (EnumOptions gamesettings$options : optionsArr) {
            if (gamesettings$options.getEnumFloat()) {
                guiSlider = new GuiSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options), this.options.getOptionFloatValue(gamesettings$options));
                this.buttonList.add(guiSlider);
            } else {
                this.buttonList.add(new GuiSmallButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), gamesettings$options, this.options.getKeyBinding(gamesettings$options)));
            }

            ++i;
        }
    }

    public void handleMouseInput() {
        super.handleMouseInput();
//        this.keyBindingList.handleMouseInput();
//        Mouse.getEventButton();
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 201) {
            for (KeyBinding keybinding : this.mc.gameSettings.keyBindings) {
                ((IGameSetting) this.options).setOptionKeyBinding(keybinding, KeyBindingExtra.getKeyCodeDefault(keybinding.keyDescription));
            }
            KeyBinding.resetKeyBindingArrayAndHash();
        } else if (button.id == 202) {
            this.mc.displayGuiScreen(new GuiControls(this.parentScreen, this.options));
            this.options.saveOptions();
        } else if (button.id < 100 && button instanceof GuiSmallButton) {
            this.options.setOptionValue(((GuiSmallButton) button).returnEnumOptions(), 1);
            button.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(button.id));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.buttonId != null) {
            ((IGameSetting) this.options).setOptionKeyBinding(this.buttonId, -100 + Mouse.getEventButton());
            System.out.println(-100 + Mouse.getEventButton());
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        } else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

//    protected void mouseReleased(int mouseX, int mouseY, int state) {
//        if (state != 0 || !this.keyBindingList.mouseReleased(mouseX, mouseY, state)) {
//            super.mouseReleased(mouseX, mouseY, state);
//        }
//    }

    protected void keyTyped(char typedChar, int keyCode) {
        if (this.buttonId != null) {
            if (keyCode == 1) {
                ((IGameSetting) this.options).setOptionKeyBinding(this.buttonId, 0);
            } else if (keyCode != 0) {
                ((IGameSetting) this.options).setOptionKeyBinding(this.buttonId, keyCode);
            } else if (typedChar > 0) {
                ((IGameSetting) this.options).setOptionKeyBinding(this.buttonId, typedChar + 256);
            }

            this.buttonId = null;
            this.time = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
        boolean flag = true;

        for (KeyBinding keybinding : this.options.keyBindings) {
            if (keybinding.keyCode != KeyBindingExtra.getKeyCodeDefault(keybinding.keyDescription)) {
                flag = false;
                break;
            }
        }

        this.buttonReset.enabled = !flag;
        super.drawScreen(mouseX, mouseY, partialTicks);
//        guiSlider.mouseReleased(mouseX, mouseY);
//        guiSlider.mousePressed(this.mc, mouseX, mouseY);
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3) {
        par3 = 0;
        super.mouseMovedOrUp(par1, par2, par3);
    }
}
