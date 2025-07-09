package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.video.GuiVideoSettings;
import moddedmite.xylose.bettergamesetting.client.gui.GuiSoundSetting;
import moddedmite.xylose.bettergamesetting.client.gui.controls.GuiNewControls;
import moddedmite.xylose.bettergamesetting.client.gui.resourcepack.GuiScreenResourcePacks;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
public class GuiOptionsMixin extends GuiScreen {
    @Mutable @Final @Shadow private static EnumOptions[] relevantOptions;
    @Shadow @Final private GameSettings options;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(300, this.width / 2 - 152, this.height / 6 + 96 - 30, 150, 20, I18n.getString("options.sounds")));
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void actionPerformed(GuiButton par1GuiButton, CallbackInfo ci) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 300) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiSoundSetting(this, this.options));
            }
            if (par1GuiButton.id == 100) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiNewControls(this, this.options));
            }
            if (par1GuiButton.id == 101) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
            }
            if (par1GuiButton.id == 105) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
            }
        }
    }

    static {
        relevantOptions = new EnumOptions[]{EnumOptions.FOV, EnumOptions.DIFFICULTY};
    }
}
