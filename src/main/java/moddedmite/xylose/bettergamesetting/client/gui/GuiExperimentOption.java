package moddedmite.xylose.bettergamesetting.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiCreateWorld;
import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;

public class GuiExperimentOption extends GuiScreen {
	private GuiScreen creteWorldGui;
	protected String screenTitle = "Experiments";
	private GuiButton skillsButton;

	public GuiExperimentOption(GuiCreateWorld createWorld) {
		this.creteWorldGui = createWorld;
	}
	
	public void initGui() {
		this.screenTitle = I18n.getString("selectWorld.experiments");
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 30, I18n.getString("gui.done")));
		this.skillsButton = new GuiButton(100, this.width / 2 + 100, 100, 40, 20, I18n.getString("options.off"));
		this.buttonList.add(this.skillsButton);
	}
	
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 200) {
				this.mc.displayGuiScreen(this.creteWorldGui);
			}
			if (button.id == 100) {
				((IGuiCreateWorld) this.creteWorldGui).switchSkillsEnable();
				this.skillsButton.displayString = ((IGuiCreateWorld) this.creteWorldGui).isSkillsEnable() ? I18n.getString("options.on") : I18n.getString("options.off");
			}
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
		this.drawCenteredSplitString(I18n.getString("selectWorld.experiments.info"), this.width / 2, 60, 0xFFFF5555, 300);
		this.drawString(this.fontRenderer, I18n.getString("selectWorld.experimental.skills"), this.width / 2 - 155, 100, 0xFFFFFFFF);
		this.drawString(this.fontRenderer, I18n.getString("selectWorld.experimental.skills.description"), this.width / 2 - 155, 110, 0xFFAAAAAA);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void drawCenteredSplitString(String text, int x, int y, int color, int width) {
		this.fontRenderer.drawSplitString(text, x - width / 2, y, width, color);
	}
}
