package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.client.gui.GuiWorldOption;
import moddedmite.xylose.bettergamesetting.client.gui.button.GuiOptionSlider;
import moddedmite.xylose.bettergamesetting.client.gui.video.GuiVideoSettings;
import moddedmite.xylose.bettergamesetting.client.gui.GuiScreenOptionsSounds;
import moddedmite.xylose.bettergamesetting.client.gui.controls.GuiNewControls;
import moddedmite.xylose.bettergamesetting.client.gui.resourcepack.GuiScreenResourcePacks;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiOptions.class)
public class GuiOptionsMixin extends GuiScreen {
    @Shadow @Mutable @Final private static EnumOptions[] relevantOptions;
    @Shadow @Final private GameSettings options;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(300, this.width / 2 - 152, this.height / 6 + 96 - 30, 150, 20, I18n.getString("options.sounds")));
        GuiButton worldOptionsButton;
        this.buttonList.add(worldOptionsButton = new GuiButton(301, this.width / 2 + 2, this.height / 6 - 12, 150, 20, I18n.getString("options.worldOptions.button")));
        worldOptionsButton.enabled = !(this.mc.getIntegratedServer() == null);
    }
    
    @WrapOperation(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean wrap(List instance, Object e, Operation<Boolean> original, @Local(name = "var5") EnumOptions var5, @Local(name = "var1") int var1) {
        this.buttonList.add(new GuiOptionSlider(
                var5.returnEnumOrdinal(),
                this.width / 2 - 155 + var1 % 2 * 160,
                this.height / 6 - 12 + 24 * (var1 >> 1),
                var5,
                var5.getValueMin(),
                var5.getValueMax(), true));
        return false;
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void actionPerformed(GuiButton par1GuiButton, CallbackInfo ci) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 300) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.options));
            }
            if (par1GuiButton.id == 301) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiWorldOption(this, this.options));
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
        relevantOptions = new EnumOptions[]{EnumOptions.FOV};
    }
}
