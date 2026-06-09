package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiCustomSlider;
import net.minecraft.*;

public class GuiSoundSetting extends GuiScreen {
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Sounds Settings";
    private GameSettings option;
    private static EnumOptions[] audioOptions = new EnumOptions[] {
            EnumOptions.MUSIC,
            EnumOptions.SOUND,
            EnumOptionsExtra.RECORDS,
            EnumOptionsExtra.WEATHER,
            EnumOptionsExtra.BLOCKS,
            EnumOptionsExtra.MOBS,
            EnumOptionsExtra.ANIMALS,
            EnumOptionsExtra.PLAYERS,
            EnumOptionsExtra.AMBIENT,
            EnumOptionsExtra.UI
    };
    public GuiSoundSetting(GuiScreen parentGuiScreen, GameSettings option) {
        this.parentGuiScreen = parentGuiScreen;
        this.option = option;
    }

    public void initGui() {
        this.screenTitle = I18n.getString("options.sounds.title");
        this.buttonList.clear();
        int j = 2;
        int audioOptionsLength = audioOptions.length;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.getString("gui.done")));

        for (int i = 0; i < audioOptionsLength; ++i) {
            EnumOptions audioOptionsSingle = audioOptions[i];
            if (audioOptionsSingle == EnumOptions.SOUND) {
                this.buttonList.add(new GuiCustomSlider(EnumOptions.SOUND.returnEnumOrdinal(), this.width / 2 - 155, this.height / 7, 310, 20, audioOptionsSingle, this.option.getKeyBinding(audioOptionsSingle), this.option.getOptionFloatValue(audioOptionsSingle)));
            } else {
                this.buttonList.add(new GuiSlider(audioOptionsSingle.returnEnumOrdinal(), this.width / 2 - 155 + j % 2 * 160, this.height / 7 + 24 * (j >> 1), audioOptionsSingle, this.option.getKeyBinding(audioOptionsSingle), this.option.getOptionFloatValue(audioOptionsSingle)));
                ++j;
            }
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
