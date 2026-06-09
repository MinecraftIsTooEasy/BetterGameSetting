package moddedmite.xylose.bettergamesetting.client.gui.gamerule;

import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.GameRules;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiTextField;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.WorldServer;
import net.xiaoyu233.fml.FishModLoader;

import java.util.Map;

public class GuiGameRules extends GuiScreen {
    private final GuiScreen parentScreen;
    final GameRules gameRules;
    private GuiGameRulesList gameRulesList;
    private GuiButton doneButton;
    private GuiButton cancelButton;
    private String screenTitle;
    private GuiTextField searchField;
    private String lastSearchText = "";

    public GuiGameRules(GuiScreen parentScreen, GameRules gameRules) {
        this.parentScreen = parentScreen;
        this.gameRules = gameRules;
    }

    @Override
    public void initGui() {
        this.screenTitle = I18n.getString("options.gameRules");
        this.searchField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 14, 200, 14);
        this.searchField.setMaxStringLength(256);
        this.searchField.setFocused(true);
        this.searchField.setHint(I18n.getString("options.search"));
        this.doneButton = new GuiButton(200, this.width / 2 - 155, this.height - 28, 150, 20, I18n.getString("gui.done"));
        this.cancelButton = new GuiButton(201, this.width / 2 + 5, this.height - 28, 150, 20, I18n.getString("gui.cancel"));
        this.buttonList.add(this.doneButton);
        this.buttonList.add(this.cancelButton);
        this.updateGameRulesList();
    }
    
    private void updateGameRulesList() {
        this.gameRulesList = new GuiGameRulesList(this, this.searchField.getText());
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.doneButton.id) {
                BGSClient.pendingRules.clear();
                for (String ruleName : BGSClient.gameRules.getRules()) {
                    BGSClient.pendingRules.put(ruleName, BGSClient.gameRules.getGameRuleStringValue(ruleName));
                }
                applyPendingRules(this.mc);
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (button.id == this.cancelButton.id) {
                this.gameRulesList.resetAllRules();
                this.mc.displayGuiScreen(this.parentScreen);
            }
        }
    }
    
    public static void applyPendingRules(Minecraft client) {
        if (client.getIntegratedServer() == null) return;
        WorldServer overworld = client.getIntegratedServer().worldServers[0];
        if (overworld != null && !BGSClient.pendingRules.isEmpty()) {
            for (Map.Entry<String, String> entry : BGSClient.pendingRules.entrySet()) {
                overworld.getGameRules().setOrCreateGameRule(entry.getKey(), entry.getValue());
                FishModLoader.LOGGER.info("Applied game rule: {} = {}", entry.getKey(), entry.getValue());
            }
            BGSClient.pendingRules.clear();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.gameRulesList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            String currentText = this.searchField.getText();
            if (!currentText.equals(this.lastSearchText)) {
                this.lastSearchText = currentText;
                this.updateGameRulesList();
            }
        } else {
            this.gameRulesList.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.gameRulesList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 4, 0xFFFFFF);
        this.searchField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.gameRulesList.updateScreen();
    }
}