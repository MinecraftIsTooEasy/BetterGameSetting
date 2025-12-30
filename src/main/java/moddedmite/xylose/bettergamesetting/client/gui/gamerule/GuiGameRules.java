package moddedmite.xylose.bettergamesetting.client.gui.gamerule;

import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.GameRules;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;

public class GuiGameRules extends GuiScreen {
    private final GuiScreen parentScreen;
    final GameRules gameRules;
    private GuiGameRulesList gameRulesList;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private String screenTitle;

    public GuiGameRules(GuiScreen parentScreen, GameRules gameRules) {
        this.parentScreen = parentScreen;
        this.gameRules = gameRules;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.getString("options.gameRules");
        this.doneButton = new GuiButton(200, this.width / 2 - 155, this.height - 28, 150, 20, I18n.getString("gui.done"));
        this.cancelButton = new GuiButton(201, this.width / 2 + 5, this.height - 28, 150, 20, I18n.getString("gui.cancel"));
        this.buttonList.add(this.doneButton);
        this.buttonList.add(this.cancelButton);
        this.gameRulesList = new GuiGameRulesList(this);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.doneButton.id) {
                BGSClient.pendingRules.clear();
                for (String ruleName : BGSClient.gameRules.getRules()) {
                    BGSClient.pendingRules.put(ruleName, BGSClient.gameRules.getGameRuleStringValue(ruleName));
                }
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == this.cancelButton.id) {
                this.gameRulesList.resetAllRules();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.gameRulesList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.gameRulesList.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.gameRulesList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.gameRulesList.updateScreen();
    }



}