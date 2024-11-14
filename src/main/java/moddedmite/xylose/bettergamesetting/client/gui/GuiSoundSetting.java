package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import net.minecraft.*;

public class GuiSoundSetting extends GuiScreen {
    private GuiScreen parentGuiScreen;
    protected String screenTitle = "Sounds Settings";
    private GameSettings guiGameSettings;
    private static EnumOptions[] audioOptions = new EnumOptions[] {EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptionsExtra.RECORDS, EnumOptionsExtra.WEATHER, EnumOptionsExtra.BLOCKS, EnumOptionsExtra.MOBS, EnumOptionsExtra.ANIMALS, EnumOptionsExtra.PLAYERS, EnumOptionsExtra.AMBIENT};

    public GuiSoundSetting(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
        this.parentGuiScreen = par1GuiScreen;
        this.guiGameSettings = par2GameSettings;
    }

    public void initGui() {
        this.screenTitle = I18n.getString("options.sounds.title");
        this.buttonList.clear();
        int var1 = 2;
        int audioOptionsLength = audioOptions.length;
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.getString("gui.done")));

        for (int i = 0; i < audioOptionsLength; ++i) {
            EnumOptions audioOptionsSingle = audioOptions[i];
            if (audioOptionsSingle == EnumOptions.SOUND) {
                this.buttonList.add(new GuiCustomSlider(EnumOptions.SOUND.returnEnumOrdinal(), this.width / 2 - 155, this.height / 7, 310, 20, audioOptionsSingle, this.guiGameSettings.getKeyBinding(audioOptionsSingle), this.guiGameSettings.getOptionFloatValue(audioOptionsSingle)));
            } else {
                this.buttonList.add(new GuiSlider(audioOptionsSingle.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, this.height / 7 + 24 * (var1 >> 1), audioOptionsSingle, this.guiGameSettings.getKeyBinding(audioOptionsSingle), this.guiGameSettings.getOptionFloatValue(audioOptionsSingle)));
                ++var1;
            }
        }
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
        super.drawScreen(par1, par2, par3);
    }
}
