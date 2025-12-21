package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.gamerule.GuiGameRuleEditor;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@SuppressWarnings("unchecked")
@Mixin(GuiCreateWorld.class)
public abstract class GuiCreateWorldMixin extends GuiScreen {

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

    @Shadow
    private void makeUseableName() {
    }

    @Shadow
    protected abstract void updateButtonText();

    @Shadow
    private GuiButton moreWorldOptions;
    @Shadow
    private boolean moreOptions;
    @Shadow
    private GuiButton button_cancel;
    @Unique
    private GuiButton buttonRulesEditor;
    @Unique
    private int currentTab = 100;
    @Unique
    private final List<GuiButton> tabButtons = new ArrayList<>();
    @Unique
    private static final int TAB_WIDTH = 130;
    @Unique
    private static final int TAB_HEIGHT = 24;
    @Unique
    private final Map<Integer, String> hoverTexts = new HashMap<>();
//    @Unique
//    private GuiButton modernWorldCreatingUI$difficultyButton;
//    @Unique
//    private EnumDifficulty modernWorldCreatingUI$difficulty = EnumDifficulty.NORMAL;

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
        this.button_skills.setPosition(this.width / 2 - 104, this.height / 5 + 50);
        this.button_skills.setSize(208, 20);

        this.buttonList.add(this.buttonRulesEditor = new GuiButton(200, this.width / 2 - 100, this.height / 6 + 2, 200, 20, I18n.getString("selectWorld.button.gameRuleEditor")));

//        this.buttonList.add(this.modernWorldCreatingUI$difficultyButton =
//                new GuiButton(9, this.width / 2 - 104, this.height / 2 + 25, 208, 20, modernWorldCreatingUI$getDifficultyText()));

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
            int xPos = startX + i * (TAB_WIDTH + 1);
            GuiButton tabButton = new GuiButton(100 + i, xPos, 4, TAB_WIDTH, TAB_HEIGHT - 4, tabNames[i]) {
                @Override
                public void drawButton(Minecraft mc, int mouseX, int mouseY) {
                    if (!this.drawButton) return;

                    boolean isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition &&
                            mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                    boolean isSelected = currentTab == this.id;

                    int effectiveHeight = this.height;
                    int yOffset = 0;
                    if (isSelected) {
                        effectiveHeight += 4;
                        yOffset = -4;
                    }

                    drawTabBorder(mc, this.xPosition, this.yPosition + yOffset, this.width, effectiveHeight, isSelected, isHovered, mc.fontRenderer.getStringWidth(this.displayString));

                    drawCenteredString(mc.fontRenderer, this.displayString,
                            this.xPosition + this.width / 2,
                            this.yPosition + yOffset + (effectiveHeight - 8) / 2,
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
                        drawRect((x + width / 2) - fontWidth / 2, underlineY, ((x + width / 2) - fontWidth / 2) + fontWidth, underlineY + 1, 0xFFFFFFFF); // 两端留出2像素空白
                    }

                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_BLEND);
                }
            };
            tabButtons.add(tabButton);
            this.buttonList.add(tabButton);
        }
    }

    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void onUpdateButtonText(CallbackInfo ci) {

//        if (this.modernWorldCreatingUI$difficultyButton != null) {
//            this.modernWorldCreatingUI$difficultyButton.displayString = modernWorldCreatingUI$getDifficultyText();
//        }

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

//    @Unique
//    private String modernWorldCreatingUI$getDifficultyText() {
//        return I18n.format("options.difficulty") + ": " +
//                I18n.format(modernWorldCreatingUI$difficulty.getDifficultyResourceKey());
//    }

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
                this.button_skills.drawButton = true;
                this.buttonRulesEditor.drawButton = false;
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
        for (Object obj : this.buttonList) {
            if (obj instanceof GuiButton button) {
                if (button.drawButton && mouseX >= button.xPosition && mouseY >= button.yPosition &&
                        mouseX < button.xPosition + button.width && mouseY < button.yPosition + button.height) {

                    if (button.id >= 100 && button.id <= 102) continue;
                    if (button.id == 0 || button.id == 1) continue;

                    if (button.id == 2) {
                        List<String> hoverText = this.getGameModeHoverText();
                        if (!hoverText.isEmpty()) {
                            ScreenUtil.getInstance().drawHoveringText(hoverText, mouseX, mouseY);
                            return;
                        }
                    }

                    String hoverText = hoverTexts.get(button.id);
                    if (hoverText != null && !hoverText.isEmpty()) {
                        ScreenUtil.getInstance().drawHoveringText(List.of(hoverText), mouseX, mouseY);
                        return;
                    }
                }
            }
        }
        if (currentTab == 100 && textboxWorldName != null) {
            if (mouseX >= textboxWorldName.xPos && mouseY >= textboxWorldName.yPos &&
                    mouseX < textboxWorldName.xPos + textboxWorldName.getWidth() && mouseY < textboxWorldName.yPos + textboxWorldName.height) {

                String worldName = textboxWorldName.getText();
                String hoverText;
                if (worldName.isEmpty()) {
                    hoverText = I18n.getStringParams("selectWorld.hover.worldName", I18n.getString("selectWorld.newWorld"));
                } else {
                    hoverText = I18n.getStringParams("selectWorld.hover.worldName", worldName);
                }

                ScreenUtil.getInstance().drawHoveringText(Collections.singletonList(hoverText), mouseX, mouseY);
            }
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
    private void onActionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id >= 100 && button.id <= 102) {
            currentTab = button.id;
            updateButtonVisibilityNAbility();
            ci.cancel();
        }

        if (button.id == 200) {
            this.mc.displayGuiScreen(new GuiGameRuleEditor(this, BGSClient.gameRules));
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

        if (button.id == 2 || button.id == 4 || button.id == 5 || button.id == 6 || button.id == 7) {
            scheduleButtonTextUpdate();
        }
    }

    @Unique
    private void handleWorldTypeSelection() {
        do {
            this.worldTypeId = (this.worldTypeId + 1) % WorldType.worldTypes.length;
        } while (WorldType.worldTypes[this.worldTypeId] == null);

        updateButtonText();
        updateButtonVisibilityNAbility();
    }

    @Unique
    private void scheduleButtonTextUpdate() {
        new Runnable() {
            @Override
            public void run() {
                updateButtonText();
            }
        };
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
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
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (currentTab == 100) {
            textboxWorldName.mouseClicked(mouseX, mouseY, mouseButton);
        } else {
            textboxSeed.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Unique
    private void drawColoredLine(int y, int width, int topColor, int bottomColor) {
        drawRect(0, y, width, y + 1, topColor);
        drawRect(0, y + 1, width, y + 2, bottomColor);
    }
}
