package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.gamerule.GuiGameRules;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.EnumOptions;
import net.minecraft.GameSettings;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiSmallButton;
import net.minecraft.I18n;

public class GuiWorldOption extends GuiScreen {
	private GuiScreen parentGuiScreen;
	protected String screenTitle = "World Options";
	private GameSettings option;
	private static EnumOptions[] options = new EnumOptions[] {
			EnumOptions.DIFFICULTY
	};
	public GuiWorldOption(GuiScreen parentGuiScreen, GameSettings option) {
		this.parentGuiScreen = parentGuiScreen;
		this.option = option;
	}
	
	public void initGui() {
		this.screenTitle = I18n.getString("options.worldOptions.title");
		this.buttonList.clear();
		int j = 2;
		int length = options.length;
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 30, I18n.getString("gui.done")));
		this.buttonList.add(new GuiButton(201, this.width / 2 + 2, 60, 150, 20, I18n.getString("editGamerule.inGame.button")));
		
		for (int i = 0; i < length; ++i) {
			EnumOptions op = options[i];
			GuiSmallButton button = new GuiSmallButton(op.returnEnumOrdinal(), this.width / 2 - 155 + j % 2 * 160, 40 + 20 * (j >> 1), op, this.option.getKeyBinding(op));
			if (op == EnumOptions.DIFFICULTY && this.mc.theWorld != null && this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
				button.enabled = false;
				button.displayString = I18n.getString("options.difficulty") + ": " + I18n.getString("options.difficulty.hardcore");
			}
			if (op == EnumOptions.DIFFICULTY) {
				button.enabled = false;
			}
			this.buttonList.add(button);
			++j;
		}
	}
	
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 200) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}
			if (button.id == 201) {
				this.mc.gameSettings.saveOptions();
				this.mc.displayGuiScreen(new GuiGameRules(this, BGSClient.gameRules));
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
