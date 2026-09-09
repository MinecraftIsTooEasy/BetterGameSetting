package moddedmite.xylose.bettergamesetting.client.gui;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import moddedmite.xylose.bettergamesetting.util.OpenALOutputLibrary;
import net.minecraft.GameSettings;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class GuiScreenOptionsSounds extends GuiScreen {
    private final GuiScreen parent;
    /**
     * Reference to the GameSettings object.
     */
    private final GameSettings game_settings_4;
    protected String title = "Options";
    private GuiScreenOptionsSounds.OptionsRowList optionsRowList;
    private String offDisplayString;

    public GuiScreenOptionsSounds(GuiScreen parentIn, GameSettings settingsIn) {
        this.parent = parentIn;
        this.game_settings_4 = settingsIn;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        this.title = I18n.getString("options.sounds.title");
        this.offDisplayString = I18n.getString("options.off");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 27, I18n.getString("gui.done")));
        this.optionsRowList = new GuiScreenOptionsSounds.OptionsRowList();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            this.mc.gameSettings.saveOptions();
        }

        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.parent);
            } else if (button.id == 201) {
                this.mc.gameSettings.setOptionValue(EnumOptionsExtra.SHOW_SUBTITLES, 1);
                button.displayString = this.mc.gameSettings.getKeyBinding(EnumOptionsExtra.SHOW_SUBTITLES);
                this.mc.gameSettings.saveOptions();
            } else if (button.id == 202) {
                this.switchToNextAudioDevice(button);
            } else if (button.id == 203) {
                this.game_settings_4.setDirectionalAudio(!this.game_settings_4.isDirectionalAudio());
                this.mc.sndManager.reloadSoundSystem();
                button.displayString = this.mc.gameSettings.getKeyBinding(EnumOptionsExtra.DIRECTIONAL_AUDIO);
                this.game_settings_4.saveOptions();
            } else if (button.id == 204) {
                this.mc.gameSettings.setOptionValue(EnumOptionsExtra.MUSIC_FREQUENCY, 1);
                button.displayString = this.mc.gameSettings.getKeyBinding(EnumOptionsExtra.MUSIC_FREQUENCY);
                this.mc.gameSettings.saveOptions();
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        super.mouseMovedOrUp(mouseX, mouseY, state);
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private String getDisplayString(SoundCategory category) {
        float f = this.game_settings_4.getSoundLevel(category);
        return f == 0.0F ? this.offDisplayString : (int) (f * 100.0F) + "%";
    }

    private SliderButton createSoundSlider(SoundCategory category, int x, boolean master) {
        return new SliderButton(category.ordinal(), x, 0, master ? 310 : 150, category);
    }

    private String getAudioDeviceButtonString() {
        String device = this.game_settings_4.getSoundDevice();
        String name;
        if (device == null || device.isEmpty()) {
            name = I18n.getString("options.audioDevice.default");
        } else {
            String decoded = OpenALOutputLibrary.decodeKeyDisplay(device);
            name = decoded.isEmpty() ? device : decoded;
        }
        return I18n.getString("options.audioDevice") + ": " + name;
    }

    private void switchToNextAudioDevice(GuiButton button) {
        String current = this.game_settings_4.getSoundDevice();
        List<String> options = Lists.newArrayList();
        options.add("");
        for (OpenALOutputLibrary.AudioDevice device : OpenALOutputLibrary.parseToken(ALC11.ALC_ALL_DEVICES_SPECIFIER)) {
            options.add(device.getKey());
        }
        int index = current == null ? -1 : options.indexOf(current);
        if (index < 0) {
            index = options.size() - 1;
        }
        String next = options.get((index + 1) % options.size());
        this.game_settings_4.setSoundDevice(next);
        this.game_settings_4.saveOptions();
        this.mc.sndManager.reloadSoundSystem();
        button.displayString = this.getAudioDeviceButtonString();
    }

    public class OptionsRowList extends GuiListExtended {
        private final List<GuiScreenOptionsSounds.Row> rows = Lists.newArrayList();

        public OptionsRowList() {
            super(GuiScreenOptionsSounds.this.mc, GuiScreenOptionsSounds.this.width, GuiScreenOptionsSounds.this.height, 32, GuiScreenOptionsSounds.this.height - 32, 25);
            this.centerListVertically = false;
            int x1 = GuiScreenOptionsSounds.this.width / 2 - 155;
            int x2 = GuiScreenOptionsSounds.this.width / 2 + 5;
            List<SoundCategory> soundcategories = Arrays.asList(SoundCategory.values());
            this.rows.add(new GuiScreenOptionsSounds.Row(GuiScreenOptionsSounds.this.createSoundSlider(soundcategories.get(0), x1, true), null));

            for (int i = 1; i < soundcategories.size(); i += 2) {
                SoundCategory left = soundcategories.get(i);
                SoundCategory right = i + 1 < soundcategories.size() ? soundcategories.get(i + 1) : null;
                this.rows.add(new GuiScreenOptionsSounds.Row(left == null ? null : GuiScreenOptionsSounds.this.createSoundSlider(left, x1, false), right == null ? null : GuiScreenOptionsSounds.this.createSoundSlider(right, x2, false)));
            }

            this.rows.add(new GuiScreenOptionsSounds.Row(new GuiButton(202, x1, 0, 310, 20, GuiScreenOptionsSounds.this.getAudioDeviceButtonString()), null));
            this.rows.add(new GuiScreenOptionsSounds.Row(new GuiOptionButton(201, x1, 0, EnumOptionsExtra.SHOW_SUBTITLES, GuiScreenOptionsSounds.this.game_settings_4.getKeyBinding(EnumOptionsExtra.SHOW_SUBTITLES)), new GuiOptionButton(203, x2, 0, EnumOptionsExtra.DIRECTIONAL_AUDIO, GuiScreenOptionsSounds.this.game_settings_4.getKeyBinding(EnumOptionsExtra.DIRECTIONAL_AUDIO))));
            this.rows.add(new GuiScreenOptionsSounds.Row(new GuiOptionButton(204, x1, 0, EnumOptionsExtra.MUSIC_FREQUENCY, GuiScreenOptionsSounds.this.game_settings_4.getKeyBinding(EnumOptionsExtra.MUSIC_FREQUENCY)), null));
        }

        public GuiScreenOptionsSounds.Row getListEntry(int index) {
            return this.rows.get(index);
        }

        protected int getSize() {
            return this.rows.size();
        }

        public int getListWidth() {
            return 400;
        }

        protected int getScrollBarX() {
            return super.getScrollBarX() + 40;
        }
    }

    public class Row implements GuiListExtended.IGuiListEntry {
        private final GuiButton buttonLeft;
        private final GuiButton buttonRight;

        public Row(GuiButton left, GuiButton right) {
            this.buttonLeft = left;
            this.buttonRight = right;
        }

        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
            if (this.buttonLeft != null) {
                this.buttonLeft.yPosition = y;
                this.buttonLeft.drawButton(GuiScreenOptionsSounds.this.mc, mouseX, mouseY);
            }

            if (this.buttonRight != null) {
                this.buttonRight.yPosition = y;
                this.buttonRight.drawButton(GuiScreenOptionsSounds.this.mc, mouseX, mouseY);
            }
        }

        public boolean mousePressed(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonLeft != null && this.buttonLeft.mousePressed(GuiScreenOptionsSounds.this.mc, x, y)) {
                this.handleButtonPressed(this.buttonLeft);
                return true;
            } else if (this.buttonRight != null && this.buttonRight.mousePressed(GuiScreenOptionsSounds.this.mc, x, y)) {
                this.handleButtonPressed(this.buttonRight);
                return true;
            } else {
                return false;
            }
        }

        private void handleButtonPressed(GuiButton button) {
            if (!(button instanceof SliderButton)) {
                GuiScreenOptionsSounds.this.actionPerformed(button);
            }
        }

        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            if (this.buttonLeft instanceof SliderButton) {
                this.buttonLeft.mouseReleased(x, y);
            }

            if (this.buttonRight instanceof SliderButton) {
                this.buttonRight.mouseReleased(x, y);
            }
        }

        @Override
        public void keyTyped(int slotIndex, char typedChar, int keyCode) {
        }

        public void setSelected(int slotIndex, int mouseX, int mouseY) {
        }
    }

    public class SliderButton extends GuiButton {
        private final SoundCategory category;
        private final String categoryName;
        public float volume = 1.0F;
        public boolean pressed;

        public SliderButton(int buttonId, int x, int y, int width, SoundCategory categoryIn) {
            super(buttonId, x, y, width, 20, "");
            this.category = categoryIn;
            this.categoryName = I18n.getString("soundCategory." + categoryIn.getName());
            this.volume = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(categoryIn);
            this.updateDisplayString();
        }

        private void updateDisplayString() {
            this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
        }

        /**
         * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering
         * over this button.
         */
        protected int getHoverState(boolean mouseOver) {
            return 0;
        }

        /**
         * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
         */
        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
            if (this.drawButton) {
                if (this.pressed) {
                    this.volume = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                    this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);
                    mc.gameSettings.setSoundLevel(this.category, this.volume);
                    mc.gameSettings.saveOptions();
                    this.updateDisplayString();
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.xPosition + (int) (this.volume * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int) (this.volume * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                this.volume = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);
                mc.gameSettings.setSoundLevel(this.category, this.volume);
                mc.gameSettings.saveOptions();
                this.updateDisplayString();
                this.pressed = true;
                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(int mouseX, int mouseY) {
            this.pressed = false;
        }
    }
}