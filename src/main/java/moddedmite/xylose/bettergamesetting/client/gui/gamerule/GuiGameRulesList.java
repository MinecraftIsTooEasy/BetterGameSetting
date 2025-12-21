package moddedmite.xylose.bettergamesetting.client.gui.gamerule;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiGameRulesList extends GuiListExtended {
    private final List<String> gameRuleNames;
    private final GuiGameRuleEditor guiGameRules;

    public GuiGameRulesList(GuiGameRuleEditor guiGameRules) {
        super(guiGameRules.mc, guiGameRules.width, guiGameRules.height, 32, guiGameRules.height - 32, 24);
        this.guiGameRules = guiGameRules;
        this.gameRuleNames = Arrays.asList(GuiGameRuleEditor.getGameRules(guiGameRules).getRules());
    }

    protected int getSize() {
        return this.gameRuleNames.size();
    }

    protected int getContentHeight() {
        return this.getSize() * 24;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 10;
    }

    protected void drawBackground() {
        this.guiGameRules.drawDefaultBackground();
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return new GameRuleEntry(this.gameRuleNames.get(index));
    }

    @Override
    protected void drawTooltip(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        String ruleName = this.gameRuleNames.get(slotIndex);
        String description = I18n.getString("gamerules." + ruleName + ".description");
        String defaultValue = I18n.getStringParams("gamerules.default", GuiGameRuleEditor.getGameRules(this.guiGameRules).getGameRuleStringValue(ruleName));
        List<String> tooltip = new ArrayList<>(List.of("§e" + ruleName));

        if (!description.equals("gamerules." + ruleName + ".description")) {
            tooltip.add(description);
        }
        tooltip.add(defaultValue);
        if (!description.isEmpty()) {
            ScreenUtil.getInstance().drawHoveringText(tooltip, mouseX, mouseY);
        }
    }

    public class GameRuleEntry implements IGuiListEntry {
        private final String ruleName;
        private final GuiButton btnToggle;

        private GameRuleEntry(String ruleName) {
            this.ruleName = ruleName;
            this.btnToggle = new GuiButton(0, 0, 0, 40, 20, "");
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            String unlocalizedRuleName= this.ruleName;
            String ruleValue = GuiGameRuleEditor.getGameRules(GuiGameRulesList.this.guiGameRules).getGameRuleStringValue(unlocalizedRuleName);

            String ruleName = I18n.getString("gamerules." + unlocalizedRuleName + ".name");
            if (ruleName.equals("gamerules." + unlocalizedRuleName + ".name")) {
                ruleName = unlocalizedRuleName;
            }
            GuiGameRulesList.this.guiGameRules.drawString(GuiGameRulesList.this.guiGameRules.mc.fontRenderer, ruleName, x + 2, y + 2, 16777215);

            this.btnToggle.xPosition = x + 190;
            this.btnToggle.yPosition = y;
            this.btnToggle.displayString = ruleValue;
            this.btnToggle.drawButton(GuiGameRulesList.this.guiGameRules.mc, mouseX, mouseY);
        }

        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            String ruleValue = GuiGameRuleEditor.getGameRules(GuiGameRulesList.this.guiGameRules).getGameRuleStringValue(this.ruleName);
            if (!this.btnToggle.mousePressed(GuiGameRulesList.this.guiGameRules.mc, x, y)) {
                GuiGameRuleEditor.getGameRules(GuiGameRulesList.this.guiGameRules)
                        .setOrCreateGameRule(this.ruleName, String.valueOf(!Boolean.parseBoolean(ruleValue)));
                return true;
            }

            return false;
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnToggle.mouseReleased(x, y);
        }

        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
        }
    }
}
