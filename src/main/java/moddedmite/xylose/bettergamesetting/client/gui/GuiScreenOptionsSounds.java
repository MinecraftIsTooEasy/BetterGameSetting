package moddedmite.xylose.bettergamesetting.client.gui;

import com.google.common.collect.Lists;
import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionButton;
import moddedmite.xylose.bettergamesetting.util.OpenALOutputLibrary;
import moddedmite.xylose.bettergamesetting.client.audio.PositionedSoundRecord;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvents;
import moddedmite.xylose.bettergamesetting.client.audio.SoundHandler;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.I18n;
import net.minecraft.GameSettings;
import net.minecraft.MathHelper;
import org.lwjgl.openal.ALC11;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiScreenOptionsSounds extends GuiScreen {
    private final GuiScreen parent;
    /**
     * Reference to the GameSettings object.
     */
    private final GameSettings game_settings_4;
    protected String title = "Options";
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
        int i = 0;
        this.buttonList.add(new GuiScreenOptionsSounds.Button(SoundCategory.MASTER.ordinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), SoundCategory.MASTER, true));
        i = i + 2;
        
        for (SoundCategory soundcategory : SoundCategory.values()) {
            if (soundcategory != SoundCategory.MASTER) {
                this.buttonList.add(new GuiScreenOptionsSounds.Button(soundcategory.ordinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 12 + 24 * (i >> 1), soundcategory, false));
                ++i;
            }
        }
        
        int j = this.width / 2 - 155;
        int l = this.width / 2 + 5;
        int k = this.height / 6 - 12;
        ++i;
        this.buttonList.add(new GuiOptionButton(201, j, k + 24 * (i >> 1), EnumOptionsExtra.SHOW_SUBTITLES, this.game_settings_4.getKeyBinding(EnumOptionsExtra.SHOW_SUBTITLES)));
        this.buttonList.add(new GuiButton(202, this.width / 2 - 155, k + 24 * (i >> 1) - 24, 310, 20, this.getAudioDeviceButtonString()));
        this.buttonList.add(new GuiOptionButton(203, l, k + 24 * (i >> 1), EnumOptionsExtra.DIRECTIONAL_AUDIO, this.game_settings_4.getKeyBinding(EnumOptionsExtra.DIRECTIONAL_AUDIO)));

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.getString("gui.done")));
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
            }
        }
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    protected String getDisplayString(SoundCategory category) {
        float f = this.game_settings_4.getSoundLevel(category);
        return f == 0.0F ? this.offDisplayString : (int) (f * 100.0F) + "%";
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
    
    class Button extends GuiButton {
        private final SoundCategory category;
        private final String categoryName;
        public float volume = 1.0F;
        public boolean pressed;
        
        public Button(int buttonId, int x, int y, SoundCategory categoryIn, boolean master) {
            super(buttonId, x, y, master ? 310 : 150, 20, "");
            this.category = categoryIn;
            this.categoryName = I18n.getString("soundCategory." + categoryIn.getName());
            this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(categoryIn);
            this.volume = GuiScreenOptionsSounds.this.game_settings_4.getSoundLevel(categoryIn);
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
                    this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
                }
                
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.xPosition + (int) (this.volume * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.xPosition + (int) (this.volume * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
            }
        }
        
        /**
         * Returns true if the mouse has been pressed on this control. Equivalent of
         * MouseListener.mousePressed(MouseEvent e).
         */
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                this.volume = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.volume = MathHelper.clamp_float(this.volume, 0.0F, 1.0F);
                mc.gameSettings.setSoundLevel(this.category, this.volume);
                mc.gameSettings.saveOptions();
                this.displayString = this.categoryName + ": " + GuiScreenOptionsSounds.this.getDisplayString(this.category);
                this.pressed = true;
                return true;
            } else {
                return false;
            }
        }
        
        public void playPressSound(SoundHandler soundHandlerIn) {
        }
        
        /**
         * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
         */
        public void mouseReleased(int mouseX, int mouseY) {
            if (this.pressed) {
                GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            
            this.pressed = false;
        }
    }
}