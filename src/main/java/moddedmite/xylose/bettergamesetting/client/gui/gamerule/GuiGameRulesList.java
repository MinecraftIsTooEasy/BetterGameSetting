package moddedmite.xylose.bettergamesetting.client.gui.gamerule;

import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiGameRulesList extends GuiListExtended {
    private final List<IGuiListEntry> entries = new ArrayList<>();
    private final GameRules gameRules;
    private RuleEntry focusedEntry = null;
    private final String searchText;
    
    public GuiGameRulesList(GuiGameRules gui, String searchText) {
        super(gui.mc, gui.width, gui.height, 32, gui.height - 32, 24);
        this.field_148163_i = false;
        this.gameRules = gui.gameRules;
        this.searchText = searchText != null ? searchText.toLowerCase() : "";
        this.buildEntriesList();
    }
    
    private void buildEntriesList() {
        this.entries.clear();
        String[] ruleNames = this.gameRules.getRules();
        List<String> sortedRules = new ArrayList<>();
	    Collections.addAll(sortedRules, ruleNames);
        
        sortedRules.sort((a, b) -> {
            RuleCategory cateA = getRuleCategory(a);
            RuleCategory cateB = getRuleCategory(b);
            if (cateA != cateB) {
                return Integer.compare(cateA.ordinal(), cateB.ordinal());
            }
            return a.compareTo(b);
        });
        
        RuleCategory currentCategory = null;
        for (String ruleName : sortedRules) {
            if (!this.searchText.isEmpty()) {
                String lowerRuleName = ruleName.toLowerCase();
                String translatedName = I18n.getString("gamerules." + ruleName + ".name").toLowerCase();
                if (!lowerRuleName.contains(this.searchText) && !translatedName.contains(this.searchText)) {
                    continue;
                }
            }
            RuleCategory category = getRuleCategory(ruleName);
            if (category != currentCategory) {
                currentCategory = category;
                this.entries.add(new CategoryEntry(category.getTranslationKey()));
            }
            boolean isBoolean = this.isBooleanRule(ruleName);
            this.entries.add(new RuleEntry(ruleName, isBoolean));
        }
    }
    
    private RuleCategory getRuleCategory(String ruleName) {
        return switch (ruleName) {
            case "keepInventory", "naturalRegeneration" -> RuleCategory.PLAYER;
            case "mobGriefing" -> RuleCategory.MOBS;
            case "doMobSpawning" -> RuleCategory.SPAWNING;
            case "doTileDrops", "doMobLoot" -> RuleCategory.DROPS;
            case "doDaylightCycle", "doFireTick" -> RuleCategory.UPDATES;
            case "commandBlockOutput" -> RuleCategory.CHAT;
            default -> RuleCategory.MISC;
        };
    }

    @Override
    protected void drawTooltip(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY) {
        if (!this.isMouseYWithinSlotBounds(mouseY)) return;
        IGuiListEntry entry = this.getListEntry(slotIndex);
        if (!(entry instanceof RuleEntry ruleEntry)) return;
        String ruleName = ruleEntry.ruleName;
        String descriptionKey = "gamerules." + ruleName + ".description";
        String description = I18n.getString(descriptionKey);
        String defaultValue = I18n.getStringParams("gamerules.default", BGSClient.DEFAULT_GAMERULE_VALUE.get(ruleName));
        List<String> tooltip = new ArrayList<>();
        tooltip.add("§e" + ruleName);
        
        if (!description.equals(descriptionKey)) {
            tooltip.add(description);
        }
        tooltip.add(defaultValue);
        
        if (!description.equals(descriptionKey) || !defaultValue.isEmpty()) {
            ScreenUtil.getInstance().drawTooltip(tooltip, mouseX, mouseY);
        }
    }

    private boolean isBooleanRule(String ruleName) {
        String value = this.gameRules.getGameRuleStringValue(ruleName);
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
    
    @Override
    public IGuiListEntry getListEntry(int index) {
        return this.entries.get(index);
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 10;
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 5;
    }

    public void resetAllRules() {
        for (IGuiListEntry entry : this.entries) {
            if (entry instanceof RuleEntry ruleEntry) {
                ruleEntry.resetToDefault();
            }
        }
    }

    public void updateScreen() {
        if (this.focusedEntry != null) {
            this.focusedEntry.updateCursorCounter();
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.isMouseYWithinSlotBounds(mouseY)) {
            this.clearFocus();
        }
        return false;
    }

    public void clearFocus() {
        if (this.focusedEntry != null) {
            this.focusedEntry.setFocused(false);
            this.focusedEntry = null;
        }
    }

    private void setFocusedEntry(RuleEntry entry) {
        if (this.focusedEntry != null && this.focusedEntry != entry) {
            this.focusedEntry.setFocused(false);
        }

        this.focusedEntry = entry;
        if (entry != null) {
            entry.setFocused(true);
        }
    }

    public class RuleEntry implements GuiListExtended.IGuiListEntry {
        private final Minecraft mc = Minecraft.getMinecraft();
        private final String ruleName;
        private final boolean isBoolean;
        private GuiButton toggleButton;
        private GuiTextField valueField;
        private boolean isFocused = false;

        public RuleEntry(String ruleName, boolean isBoolean) {
            this.ruleName = ruleName;
            this.isBoolean = isBoolean;

            if (isBoolean) {
                this.toggleButton = new GuiButton(0, 0, 0, 40, 20, "");
                this.updateToggleButtonText();
            } else {
                this.valueField = new GuiTextField(this.mc.fontRenderer, 0, 0, 40, 20);
                this.valueField.setMaxStringLength(256);
                this.valueField.setText(gameRules.getGameRuleStringValue(ruleName));
            }
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            int nameX = x + 10;
            int nameY = y + (slotHeight - 8) / 2;
            String unlocalizedRuleName = this.ruleName;

            String ruleName = I18n.getString("gamerules." + unlocalizedRuleName + ".name");
            if (ruleName.equals("gamerules." + unlocalizedRuleName + ".name")) {
                ruleName = unlocalizedRuleName;
            }
            this.mc.fontRenderer.drawString(ruleName, nameX, nameY, 0xFFFFFFFF);

            int controlX = x + listWidth - 35;
            int controlY = y + (slotHeight - 20) / 2;

            if (this.isBoolean) {
                this.toggleButton.xPosition = controlX;
                this.toggleButton.yPosition = controlY;
                this.toggleButton.drawButton(this.mc, mouseX, mouseY);
            } else {
                this.valueField.setPosition(controlX, controlY);
                this.valueField.drawTextBox();
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.isBoolean) {
                if (this.toggleButton.mousePressed(this.mc, x, y)) {
                    boolean currentValue = gameRules.getGameRuleBooleanValue(this.ruleName);
                    gameRules.setOrCreateGameRule(this.ruleName, String.valueOf(!currentValue));
                    this.updateToggleButtonText();
                    GuiGameRulesList.this.clearFocus();
                    return true;
                }
            } else {
                boolean clickedTextField = valueField.isMouseOver();
                if (clickedTextField) {
                    setFocusedEntry(this);
                    this.valueField.mouseClicked(x, y, mouseEvent);
                    return true;
                } else {
                    this.setFocused(false);
                    if (focusedEntry == this) {
                        focusedEntry = null;
                    }
                }
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.isBoolean) {
                this.toggleButton.mouseReleased(x, y);
            }
        }

        @Override
        public void keyTyped(int slotIndex, char typedChar, int keyCode) {
            if (this == focusedEntry && !this.isBoolean && this.valueField.isFocused()) {
                if (keyCode == Keyboard.KEY_RETURN) {
                    this.saveTextFieldValue();
                    this.setFocused(false);
                    focusedEntry = null;
                } else if (keyCode == Keyboard.KEY_ESCAPE) {
                    this.valueField.setText(gameRules.getGameRuleStringValue(this.ruleName));
                    this.setFocused(false);
                    focusedEntry = null;
                } else {
                    String oldText = this.valueField.getText();
                    this.valueField.textboxKeyTyped(typedChar, keyCode);

                    if (!oldText.equals(this.valueField.getText())) {
                        this.saveTextFieldValue();
                    }
                }
            }
        }

        @Override
        public void setSelected(int slotIndex, int mouseX, int mouseY) {
        }

        private void saveTextFieldValue() {
            String value = this.valueField.getText();
            gameRules.setOrCreateGameRule(this.ruleName, value);
        }

        public void updateCursorCounter() {
            if (!this.isBoolean && this.valueField != null && this.isFocused) {
                this.valueField.updateCursorCounter();
            }
        }

        private void updateToggleButtonText() {
            if (this.toggleButton != null) {
                boolean value = gameRules.getGameRuleBooleanValue(this.ruleName);
                this.toggleButton.displayString = (value ? I18n.getString("options.on") : I18n.getString("options.off"));
            }
        }

        public void resetToDefault() {
            String defaultValue = BGSClient.DEFAULT_GAMERULE_VALUE.get(this.ruleName);
            gameRules.setOrCreateGameRule(this.ruleName, defaultValue);
            this.updateToggleButtonText();
            if (this.valueField != null) {
                this.valueField.setText(defaultValue);
            }
        }

        public void setFocused(boolean focused) {
            this.isFocused = focused;
            if (this.valueField != null) {
                this.valueField.setFocused(focused);
            }
        }

        public boolean isFocused() {
            return this.isFocused;
        }
    }
    
    public class CategoryEntry implements IGuiListEntry {
        private final String labelText;
        private final int labelWidth;
        
        public CategoryEntry(String label) {
            this.labelText = I18n.getString(label);
            this.labelWidth = GuiGameRulesList.this.client.fontRenderer.getStringWidth(this.labelText);
        }
        
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            GuiGameRulesList.this.client.fontRenderer.drawString(EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD + this.labelText, GuiGameRulesList.this.client.currentScreen.width / 2 - this.labelWidth / 2, y + slotHeight - GuiGameRulesList.this.client.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }
        
        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            return false;
        }
        
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
        }
        
        @Override
        public void keyTyped(int slotIndex, char typedChar, int keyCode) {
        }
        
        public void setSelected(int slotIndex, int mouseX, int mouseY) {
        }
    }
    
    private enum RuleCategory {
        PLAYER("player"),
        MOBS("mobs"),
        SPAWNING("spawning"),
        DROPS("drops"),
        UPDATES("updates"),
        CHAT("chat"),
        MISC("misc");
        
        private final String keyName;
        
        RuleCategory(String keyName) {
            this.keyName = keyName;
        }
        
        public String getTranslationKey() {
            return "gamerules.category." + this.keyName;
        }
    }
}