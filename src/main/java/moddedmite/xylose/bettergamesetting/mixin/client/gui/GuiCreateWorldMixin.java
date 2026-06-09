package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.ICreateWorld;
import moddedmite.xylose.bettergamesetting.client.gui.GuiExperimentOption;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiTabButton;
import moddedmite.xylose.bettergamesetting.client.gui.gamerule.GuiGameRules;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.*;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@SuppressWarnings("unchecked")
@Mixin(GuiCreateWorld.class)
public abstract class GuiCreateWorldMixin extends GuiScreen implements ICreateWorld {
    @Shadow private GuiScreen parentGuiScreen;
    @Shadow private boolean isHardcore;
    @Shadow private String localizedNewWorldText;
    @Shadow private String gameMode;
    @Shadow private String seed;
    @Shadow private boolean generateStructures;
    @Shadow private boolean bonusItems;
    @Shadow private boolean commandsAllowed;
    @Shadow private GuiTextField textboxSeed;
    @Shadow private GuiTextField textboxWorldName;
    @Shadow private GuiButton buttonAllowCommands;
    @Shadow private GuiButton buttonGameMode;
    @Shadow private GuiButton buttonBonusItems;
    @Shadow private GuiButton buttonWorldType;
    @Shadow private GuiButton buttonGenerateStructures;
    @Shadow private GuiButton buttonCustomize;
    @Shadow private int worldTypeId;
    @Shadow private boolean commandsToggled;
    @Shadow private GuiButton button_skills;
    @Shadow private GuiButton moreWorldOptions;
    @Shadow private boolean moreOptions;
    @Shadow private GuiButton button_cancel;
    @Shadow private void makeUseableName() {}
    @Shadow protected abstract void updateButtonText();
    
    @Shadow private boolean are_skills_enabled;
    @Unique private GuiButton buttonRulesEditor;
    @Unique private GuiButton buttonExperiments;
    @Unique private GuiButton buttonDifficulty;
    @Unique private int currentTab = 100;
    @Unique private final List<GuiButton> tabButtons = new ArrayList<>();
    @Unique private static final int TAB_WIDTH = 130;
    @Unique private static final int TAB_HEIGHT = 24;
    @Unique private final Map<Integer, String> hoverTexts = new HashMap<>();

    @Inject(method = "initGui", at = @At("TAIL"))
    private void onInitGuiTail(CallbackInfo ci) {
        this.buttonList.remove(this.moreWorldOptions);

        createTabButtons();
        recreateButtons();
        setupTextFields();
        updateButtonVisibilityNAbility();
        repositionActionButtons();
        initHoverTexts();
        updateButtonText();

        this.buttonWorldType.enabled = true;
        this.buttonBonusItems.enabled = true;
        this.buttonAllowCommands.enabled = true;
    }

    @Unique
    private void initHoverTexts() {
        hoverTexts.put(4, I18n.getString("selectWorld.mapFeatures.info"));
        hoverTexts.put(6, I18n.getString("selectWorld.allowCommands.info"));
        hoverTexts.put(9, I18n.getString("selectWorld.professions.info"));
    }

    @Unique
    private void recreateButtons() {
        this.buttonGameMode.setPosition(this.width / 2 - 104, this.height / 5 + 25);
        this.buttonGameMode.setSize(208, 20);
        this.buttonGenerateStructures.setPosition(this.width / 2 + 154 - 44, this.height / 8 + 90);
        this.buttonGenerateStructures.setSize(44, 20);
        this.buttonBonusItems.setPosition(this.width / 2 + 154 - 44, this.height / 8 + 115);
        this.buttonBonusItems.setSize(44, 20);
        this.buttonWorldType.setPosition(this.width / 2 - 154, this.height / 8 + 10);
        this.buttonWorldType.setSize(150, 20);
        this.buttonAllowCommands.setPosition(this.width / 2 - 104, this.height / 5 + 75);
        this.buttonAllowCommands.setSize(208, 20);
        this.buttonCustomize.setPosition(this.width / 2 + 4, this.height / 8 + 10);
        this.buttonCustomize.setSize(150, 20);
//        this.button_skills.setPosition(this.width / 2 - 104, this.height / 5 + 50);
//        this.button_skills.setSize(208, 20);

        this.buttonList.add(this.buttonRulesEditor = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 2, 200, 20, I18n.getString("selectWorld.button.gameRuleEditor")));
        this.buttonList.add(this.buttonExperiments = new GuiButton(201, this.width / 2 - 100, this.height / 6 + 30, 200, 20, I18n.getString("selectWorld.experiments")));
        this.buttonList.add(this.buttonDifficulty = new GuiButton(202, this.width / 2 - 104, this.height / 5 + 50, 208, 20, I18n.getString("options.difficulty") + ": " + I18n.getString("options.difficulty.hardcore")));
        this.buttonDifficulty.enabled = false;

        updateButtonText();
    }

    @Unique
    private void setupTextFields() {
        if (textboxWorldName != null) {
            textboxWorldName.setPosition(this.width / 2 - 104, this.height / 5);
            textboxWorldName.setSize(208, textboxWorldName.height);
        } else {
            textboxWorldName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, TAB_HEIGHT + 10, this.width / 2, 20);
            textboxWorldName.setText(this.localizedNewWorldText);
        }

        if (textboxSeed != null) {
            textboxSeed.setPosition(this.width / 2 - 154, this.height / 8 + 50);
            textboxSeed.setSize(308, textboxSeed.height);
            textboxSeed.setHint(I18n.getString("selectWorld.seedInfo"));
        } else {
            textboxSeed = new GuiTextField(this.fontRenderer, this.width / 2 - 100, TAB_HEIGHT + 20, this.width / 2, 20);
            textboxSeed.setText(this.seed);
        }

        textboxWorldName.setFocused(true);
    }

    @Unique
    private void repositionActionButtons() {
        GuiButton createButton = ScreenUtil.getInstance().getButtonById(0, this.buttonList);

        if (createButton != null) {
            createButton.setPosition(this.width / 2 - 155, this.height - 28);
            createButton.setSize(150, 20);
        }

        if (button_cancel != null) {
            this.button_cancel.setPosition(this.width / 2 + 5, this.height - 28);
            this.button_cancel.setSize(150, 20);
        }
    }

    @Unique
    private void createTabButtons() {
        this.tabButtons.clear();

        int totalWidth = TAB_WIDTH * 3 + 2;
        int startX = this.width / 2 - totalWidth / 2;
        String[] tabNames = {
                I18n.getString("selectWorld.tab.game"),
                I18n.getString("selectWorld.tab.world"),
                I18n.getString("selectWorld.tab.more")
        };

        for (int i = 0; i < 3; i++) {
            int xPos = startX + i * (TAB_WIDTH);
            GuiButton tabButton = new GuiTabButton(100 + i, xPos, 4, TAB_WIDTH, TAB_HEIGHT - 4, tabNames[i]);
            tabButtons.add(tabButton);
            this.buttonList.add(tabButton);
        }
    }

    @Unique
    private void drawTabButton(GuiButton button, Minecraft mc, int mouseX, int mouseY) {
        if (!button.drawButton) return;

        boolean isSelected = currentTab == button.id;
        int displayY = button.yPosition;
        int displayHeight = button.height;

        if (isSelected) {
            displayY -= 4;
            displayHeight += 4;
        }

        boolean isHovered = mouseX >= button.xPosition &&
                           mouseY >= displayY &&
                           mouseX < button.xPosition + button.width &&
                           mouseY < displayY + displayHeight;

        drawTabBorder(mc, button.xPosition, displayY, button.width, displayHeight, isSelected, isHovered,
                mc.fontRenderer.getStringWidth(button.displayString));

        drawCenteredString(mc.fontRenderer, button.displayString,
                button.xPosition + button.width / 2,
                displayY + (displayHeight - 8) / 2,
                0xFFFFFF);
    }

    @Unique
    private void drawTabBorder(Minecraft client, int x, int y, int width, int height, boolean isSelected, boolean isHovered, int fontWidth) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        final int WHITE = isHovered ? 0xFFFFFFFF : 0x66ADB1B1;
        final int BLACK = 0xCC000000;

        int bgColor = isSelected ? 0x00FFFFFFF : 0xDD000000;
        int heightAdjust = isSelected ? 1 : 2;
        if (client.gameSettings.isTransparentBackground())
            drawRect(x + 2, y + 2, x + width - 2, y + height - 2, bgColor);

        drawRect(x, y, x + width, y + 1, BLACK);//top
        drawRect(x, y, x + 1, y + height - 2, BLACK);//left
        drawRect(x + width - 1, y, x + width, y + height - 2, BLACK);//right

        drawRect(x + 1, y + 1, x + width - 1, y + 2, WHITE);//top
        drawRect(x + 1, y + 2, x + 2, y + height - heightAdjust, WHITE);//left
        drawRect(x + width - 2, y + 2, x + width - 1, y + height - heightAdjust, WHITE);//right
        if (isHovered && !isSelected)
            drawRect(x + 2, y + height - 3, x + width - 2, y + height - 2, WHITE);//bottom

        if (isSelected) {
            int underlineY = y + height - 3;
            drawRect((x + width / 2) - fontWidth / 2, underlineY, ((x + width / 2) - fontWidth / 2) + fontWidth, underlineY + 1, 0xFFFFFFFF);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }


    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void onUpdateButtonText(CallbackInfo ci) {
        this.buttonGameMode.displayString = I18n.getString("selectWorld.gameMode") + " " +
                I18n.getString("selectWorld.gameMode." + this.gameMode);

        this.buttonGenerateStructures.displayString =
                (this.generateStructures ? I18n.getString("options.on") : I18n.getString("options.off"));

        this.buttonBonusItems.displayString =
                (this.bonusItems && !this.isHardcore ? I18n.getString("options.on") : I18n.getString("options.off"));

        this.buttonWorldType.displayString = I18n.getString("selectWorld.mapType") + " " +
                I18n.getString(WorldType.worldTypes[this.worldTypeId].getTranslateName());

        this.buttonAllowCommands.displayString = I18n.getString("selectWorld.allowCommands") + " " +
                (this.commandsAllowed && !this.isHardcore ? I18n.getString("options.on") : I18n.getString("options.off"));
    }

    @Unique
    private void updateButtonVisibilityNAbility() {
        switch (currentTab) {
            case 100:
                this.buttonGameMode.drawButton = true;
                this.buttonAllowCommands.drawButton = true;
                this.buttonGenerateStructures.drawButton = false;
                this.buttonBonusItems.drawButton = false;
                this.buttonWorldType.drawButton = false;
                this.buttonCustomize.drawButton = false;
                this.button_skills.drawButton = false;//moved to experiment screen
                this.buttonRulesEditor.drawButton = false;
                this.buttonExperiments.drawButton = false;
                this.buttonDifficulty.drawButton = true;
                this.moreOptions = false;//for compat
                break;
            case 101:
                this.buttonGameMode.drawButton = false;
                this.buttonAllowCommands.drawButton = false;
                this.buttonGenerateStructures.drawButton = true;
                this.buttonBonusItems.drawButton = true;
                this.buttonWorldType.drawButton = true;
                this.buttonCustomize.enabled = WorldType.worldTypes[this.worldTypeId] == WorldType.FLAT;
                this.buttonCustomize.drawButton = true;
                this.button_skills.drawButton = false;
                this.buttonRulesEditor.drawButton = false;
                this.buttonExperiments.drawButton = false;
                this.buttonDifficulty.drawButton = false;
                this.moreOptions = false;//for compat
                break;
            case 102:
                this.buttonGameMode.drawButton = false;
                this.buttonAllowCommands.drawButton = false;
                this.buttonGenerateStructures.drawButton = false;
                this.buttonBonusItems.drawButton = false;
                this.buttonWorldType.drawButton = false;
                this.buttonCustomize.drawButton = false;
                this.button_skills.drawButton = false;
                this.buttonRulesEditor.drawButton = true;
                this.buttonExperiments.drawButton = true;
                this.buttonDifficulty.drawButton = false;
                this.moreOptions = true;//for compat
                break;
        }

        updateButtonText();
    }

    /**
     * @author dfdvdsf
     * @reason Enhance the vanilla
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);

//        if (!this.mc.gameSettings.isTransparentBackground())
//            drawRect(0, 0, this.width, TAB_HEIGHT - 2, 0xFF000000);

        drawTabSeparatorLine();
        drawColoredLine(this.height - 35, this.width, 0xCC000000, 0x66ADB1B1);

        for (GuiButton tabButton : this.tabButtons) {
            drawTabButton(tabButton, this.mc, mouseX, mouseY);
        }

        if (currentTab == 100) {
            this.drawString(this.fontRenderer, I18n.getString("selectWorld.enterName"), this.width / 2 - 104, this.height / 5 - 13, 0xFFFFFF);
            textboxWorldName.drawTextBox();
        }

        if (currentTab == 101) {
            this.drawString(this.fontRenderer, I18n.getString("selectWorld.enterSeed"), this.width / 2 - 154, this.height / 8 + 50 - 13, 0xFFFFFF);
            textboxSeed.drawTextBox();
            String mapFeatures = I18n.getString("selectWorld.mapFeatures");
            String bonusItems = I18n.getString("selectWorld.bonusItems");
            this.drawString(this.fontRenderer, mapFeatures.substring(0, mapFeatures.length() - 1), this.width / 2 - 154, this.height / 8 + 90, 0xFFFFFF);
            this.drawString(this.fontRenderer, bonusItems.substring(0, bonusItems.length() - 1), this.width / 2 - 154, this.height / 8 + 110, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawHoverText(mouseX, mouseY);
    }

    @Unique
    private void drawTabSeparatorLine() {
        GuiButton selectedTab = null;
        for (GuiButton tabButton : this.tabButtons) {
            if (tabButton.id == currentTab) {
                selectedTab = tabButton;
                break;
            }
        }

        if (selectedTab != null) {
            if (selectedTab.xPosition > 0) {
                drawRect(0, TAB_HEIGHT - 1, selectedTab.xPosition + 2, TAB_HEIGHT, 0xCC000000);
                drawRect(0, TAB_HEIGHT - 2, selectedTab.xPosition + 1, TAB_HEIGHT - 1, 0x66ADB1B1);
            }

            int rightStart = selectedTab.xPosition + selectedTab.width;
            if (rightStart < this.width) {
                drawRect(rightStart - 2, TAB_HEIGHT - 1, this.width, TAB_HEIGHT, 0xCC000000);
                drawRect(rightStart - 1, TAB_HEIGHT - 2, this.width, TAB_HEIGHT - 1, 0x66ADB1B1);
            }
        } else {
            drawColoredLine(TAB_HEIGHT - 2, this.width, 0x66ADB1B1, 0xCC000000);
        }
    }


    @Unique
    private void drawHoverText(int mouseX, int mouseY) {
        for (GuiButton button : (List<GuiButton>) this.buttonList) {
            if (!button.func_82252_a()/*isMouseOver*/) continue;
            if (button.id >= 100 && button.id <= 102) continue;
            if (button.id == 0 || button.id == 1) continue;
            
            if (button.id == 2) {
                List<String> hoverText = this.getGameModeHoverText();
                if (!hoverText.isEmpty()) {
                    ScreenUtil.getInstance().drawTooltip(hoverText, mouseX, mouseY);
                    return;
                }
            }
            
            String hoverText = hoverTexts.get(button.id);
            if (hoverText != null && !hoverText.isEmpty()) {
                ScreenUtil.getInstance().drawTooltip(List.of(hoverText), mouseX, mouseY);
                return;
            }
        }
        if (currentTab == 100 && textboxWorldName != null) {
            if (!textboxWorldName.isMouseOver()) return;
            String worldName = textboxWorldName.getText();
            String hoverText;
            if (worldName.isEmpty()) {
                hoverText = I18n.getStringParams("selectWorld.hover.worldName", I18n.getString("selectWorld.newWorld"));
            } else {
                hoverText = I18n.getStringParams("selectWorld.hover.worldName", worldName);
            }
            
            ScreenUtil.getInstance().drawTooltip(Collections.singletonList(hoverText), mouseX, mouseY);
        }
        if (currentTab == 101 && textboxSeed != null) {
            if (!textboxSeed.isMouseOver()) return;
            ScreenUtil.getInstance().drawTooltip(Collections.singletonList(I18n.getString("selectWorld.hover.seed")), mouseX, mouseY);
        }
    }

    @Unique
    private List<String> getGameModeHoverText() {
        return switch (this.gameMode) {
            case "survival" ->
                    List.of(I18n.getString("selectWorld.gameMode.survival.line1"), I18n.getString("selectWorld.gameMode.survival.line2"));
            case "creative" ->
                    List.of(I18n.getString("selectWorld.gameMode.creative.line1"), I18n.getString("selectWorld.gameMode.creative.line2"));
            case "hardcore" -> List.of(I18n.getString("selectWorld.hardcoreMode.info"));
            case "adventure" ->
                    List.of(I18n.getString("selectWorld.gameMode.adventure.line1"), I18n.getString("selectWorld.gameMode.adventure.line2"));
            default -> List.of(I18n.getString("selectWorld.gameMode"));
        };
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void onButtonAction(GuiButton button, CallbackInfo ci) {
        if (button.id >= 100 && button.id <= 102) {
            currentTab = button.id;
            updateButtonVisibilityNAbility();
            ci.cancel();
        }

        if (button.id == 200) {
            this.mc.displayGuiScreen(new GuiGameRules(this, BGSClient.gameRules));
            ci.cancel();
        }

        if (button.id == 201) {
            this.mc.displayGuiScreen(new GuiExperimentOption(ReflectHelper.dyCast(this)));
            ci.cancel();
        }

        if (button.id == 5) {
            handleWorldTypeSelection();
            this.updateButtonText();
            ci.cancel();
        }

        if (button.id == 7) {
            this.bonusItems = !this.bonusItems;
            this.updateButtonText();
            ci.cancel();
        }
        if (button.id == 6) {
            this.commandsToggled = true;
            this.commandsAllowed = !this.commandsAllowed;
            this.updateButtonText();
            ci.cancel();
        }
        
        if (button.id == 2) {
            updateGameMode();
            updateButtonText();
            ci.cancel();
        }

        if (button.id == 4 || button.id == 5 || button.id == 6 || button.id == 7) {
            updateButtonText();
        }
    }

    @Unique
    private void updateGameMode() {
        if (this.gameMode.equals("survival")) {
            if (!this.commandsToggled) {
                this.commandsAllowed = false;
            }

            this.isHardcore = false;
            this.gameMode = "hardcore";
            this.isHardcore = true;
            this.buttonAllowCommands.enabled = false;
            this.buttonBonusItems.enabled = false;
        } else if (this.gameMode.equals("hardcore") && (Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get())) {
            if (!this.commandsToggled) {
                this.commandsAllowed = true;
            }

            this.isHardcore = false;
            this.gameMode = "creative";
            this.buttonAllowCommands.enabled = true;
            this.buttonBonusItems.enabled = true;
        } else {
            if (!this.commandsToggled) {
                this.commandsAllowed = false;
            }

            this.gameMode = "survival";
            this.buttonAllowCommands.enabled = true;
            this.buttonBonusItems.enabled = true;
            this.isHardcore = false;
        }

        this.updateButtonText();
    }

    @Unique
    private void handleWorldTypeSelection() {
        do {
            this.worldTypeId = (this.worldTypeId + 1) % WorldType.worldTypes.length;
        } while (WorldType.worldTypes[this.worldTypeId] == null || !WorldType.worldTypes[this.worldTypeId].getCanBeCreated());

        updateButtonText();
        updateButtonVisibilityNAbility();
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void onKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        if (currentTab == 100) {
            textboxWorldName.textboxKeyTyped(typedChar, keyCode);
            this.localizedNewWorldText = this.textboxWorldName.getText();
        } else {
            textboxSeed.textboxKeyTyped(typedChar, keyCode);
            this.seed = this.textboxSeed.getText();
        }

        if (keyCode == 1) {
            this.mc.displayGuiScreen(parentGuiScreen);
        }

        ((GuiButton) this.buttonList.get(2)).enabled = !this.textboxWorldName.getText().isEmpty();

        this.makeUseableName();

        ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (currentTab == 100) {
            textboxWorldName.mouseClicked(mouseX, mouseY, mouseButton);
        } else {
            textboxSeed.mouseClicked(mouseX, mouseY, mouseButton);
        }

        ci.cancel();
    }

    @Unique
    private void drawColoredLine(int y, int width, int topColor, int bottomColor) {
        drawRect(0, y, width, y + 1, topColor);
        drawRect(0, y + 1, width, y + 2, bottomColor);
    }

    @Unique
    @Override
    public void switchSkillsEnable() {
        this.are_skills_enabled = !this.are_skills_enabled;
    }

    @Unique
    @Override
    public boolean isSkillsEnable() {
        return this.are_skills_enabled;
    }
}
