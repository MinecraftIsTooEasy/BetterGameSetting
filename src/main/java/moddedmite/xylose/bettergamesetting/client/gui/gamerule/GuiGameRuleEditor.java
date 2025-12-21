package moddedmite.xylose.bettergamesetting.client.gui.gamerule;

import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.*;

public class GuiGameRuleEditor extends GuiScreen {
    protected GuiScreen parentGui;
    private GuiGameRulesList gameRuleList;
    public final GameRules gameRules;

    public GuiGameRuleEditor(GuiScreen parentGui, GameRules gameRules) {
        this.parentGui = parentGui;
        this.gameRules = gameRules;
    }

    public void initGui() {
        this.buttonList.add(new GuiSmallButton(200, this.width / 2 - 75, this.height - 24, I18n.getString("gui.done")));
        this.gameRuleList = new GuiGameRulesList(this);
        this.gameRuleList.registerScrollButtons(7, 8);
    }

    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 200) {
                BGSClient.pendingRules.clear();
                for (String ruleName : BGSClient.gameRules.getRules()) {
                    BGSClient.pendingRules.put(ruleName, BGSClient.gameRules.getGameRuleStringValue(ruleName));
                }
                this.mc.displayGuiScreen(this.parentGui);
            } else {
                this.gameRuleList.actionPerformed(par1GuiButton);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.gameRuleList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int par1, int par2, float par3) {
        this.gameRuleList.drawScreen(par1, par2, par3);
        this.drawCenteredString(this.fontRenderer, I18n.getString("options.gameRules"), this.width / 2, 16, 16777215);
        super.drawScreen(par1, par2, par3);
    }

    static GameRules getGameRules(GuiGameRuleEditor gui) {
        return gui.gameRules;
    }
}