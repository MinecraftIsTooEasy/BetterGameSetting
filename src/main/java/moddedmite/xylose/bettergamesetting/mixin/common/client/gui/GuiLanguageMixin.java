package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiLanguage.class)
public class GuiLanguageMixin extends GuiScreen {
    @Shadow private GuiSmallButton doneButton;
    @Shadow @Final private GameSettings theGameSettings;

    @Unique private GuiSmallButton forceUnicodeButton;


    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean redirect(List instance, Object e) {
        this.buttonList.add(this.forceUnicodeButton = new GuiSmallButton(100, this.width / 2 - 155, this.height - 38, EnumOptionsExtra.FORCE_UNICODE_FONT, this.theGameSettings.getKeyBinding(EnumOptionsExtra.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.doneButton = new GuiSmallButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.getString("gui.done")));
        return true;
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton guiButton, CallbackInfo ci) {
        if (guiButton.enabled && guiButton.id == 100) {
            this.theGameSettings.setOptionValue(EnumOptionsExtra.FORCE_UNICODE_FONT, 1);
            guiButton.displayString = this.theGameSettings.getKeyBinding(EnumOptionsExtra.FORCE_UNICODE_FONT);
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, i, j);
        }
    }
}
