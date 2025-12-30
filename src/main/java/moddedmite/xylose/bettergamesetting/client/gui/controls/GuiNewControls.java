package moddedmite.xylose.bettergamesetting.client.gui.controls;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiYesNoModern;
import net.minecraft.*;
import org.lwjgl.input.Mouse;

public class GuiNewControls extends GuiScreen {
    private static final EnumOptions[] optionsArr = new EnumOptions[]{EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.TOUCHSCREEN};
    private GuiScreen parentScreen;
    protected String screenTitle = "Controls";
    private GameSettings options;
    public KeyBinding binding = null;
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
        this.buttonList.add(new GuiButton(200, this.width / 2 + 5, this.height - 29, 150, 20, I18n.getString("gui.done")));
        this.buttonList.add(this.buttonReset = new GuiButton(201, this.width / 2 - 155, this.height - 29, 150, 20, I18n.getString("controls.resetAll")));
        this.buttonList.add(new GuiButton(202, this.width / 2 + 5, 18 + 22, 150, 20, I18n.getString("controls.classicControls")));
        this.screenTitle = I18n.getString("controls.title");
        int i = 0;

        for (EnumOptions options : optionsArr) {
            if (options.getEnumFloat()) {
                guiSlider = new GuiSlider(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 22 * (i >> 1), options, this.options.getKeyBinding(options), this.options.getOptionFloatValue(options));
                this.buttonList.add(guiSlider);
            } else {
                this.buttonList.add(new GuiSmallButton(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 22 * (i >> 1), options, this.options.getKeyBinding(options)));
            }

            ++i;
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 201) {
            this.mc.displayGuiScreen(new GuiYesNoModern((result, id) -> {
                this.mc.displayGuiScreen(this);
                if (result) {
                    for (KeyBinding keybinding : this.mc.gameSettings.keyBindings) {
                        this.mc.gameSettings.setOptionKeyBinding(keybinding, keybinding.getDefaultKeyCode(keybinding.keyDescription));
                    }
                    KeyBinding.resetKeyBindingArrayAndHash();
                }
            }, I18n.getString("controls.reset_keybinding_info"), 0));        } else if (button.id == 202) {
            this.mc.displayGuiScreen(new GuiControls(this, this.options));
            this.options.saveOptions();
        } else if (button.id < 100 && button instanceof GuiSmallButton) {
            this.options.setOptionValue(((GuiSmallButton) button).returnEnumOptions(), 1);
            button.displayString = this.options.getKeyBinding(EnumOptions.getEnumOptions(button.id));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.binding != null) {
            this.options.setOptionKeyBinding(this.binding, -100 + Mouse.getEventButton());
            this.binding = null;
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
        if (this.binding != null) {
            if (keyCode == 1) {
                this.options.setOptionKeyBinding(this.binding, 0);
            } else if (keyCode != 0) {
                this.options.setOptionKeyBinding(this.binding, keyCode);
            } else if (typedChar > 0) {
                this.options.setOptionKeyBinding(this.binding, typedChar + 256);
            }

            this.binding = null;
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
            if (keybinding.keyCode != keybinding.getDefaultKeyCode(keybinding.keyDescription)) {
                flag = false;
                break;
            }
        }

        this.buttonReset.enabled = !flag;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3) {
        par3 = 0;
        super.mouseMovedOrUp(par1, par2, par3);
    }
}
