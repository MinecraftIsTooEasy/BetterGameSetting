package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.client.EnumOptionsExtra;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.mixin.client.invoker.GuiSlotLanguageInvoker;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiLanguage.class)
public class GuiLanguageMixin extends GuiScreen {
    @Shadow private GuiSmallButton doneButton;
    @Shadow @Final private GameSettings theGameSettings;
    @Shadow private GuiSlotLanguage languageList;

    @Unique private GuiTextField searchField;

    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean redirect(List instance, Object e) {
        this.buttonList.add(new GuiSmallButton(100, this.width / 2 - 155, this.height - 38, EnumOptionsExtra.FORCE_UNICODE_FONT, this.theGameSettings.getKeyBinding(EnumOptionsExtra.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.doneButton = new GuiSmallButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.getString("gui.done")));
        this.searchField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 14, 200, 15);
        this.searchField.setMaxStringLength(50);
        this.searchField.setHint(I18n.getString("options.search"));
        return true;
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    private void onInitTail(CallbackInfo ci) {
        this.languageList.func_77208_b(BGSClient.scrollAmount);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.enabled && button.id == 100) {
            this.theGameSettings.setOptionValue(EnumOptionsExtra.FORCE_UNICODE_FONT, 1);
            button.displayString = this.theGameSettings.getKeyBinding(EnumOptionsExtra.FORCE_UNICODE_FONT);
            ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            this.setWorldAndResolution(this.mc, i, j);
        }
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void addSearchField(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.searchField.drawTextBox();
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiLanguage;drawCenteredString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 0), index = 3)
    private int modifyTitleY(int originalX1) {
        return 4;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            ((GuiSlotLanguageInvoker) this.languageList).invokeUpdateFilteredLanguages(this.searchField.getText());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int eventButton) {
        super.mouseClicked(mouseX, mouseY, eventButton);
        this.searchField.mouseClicked(mouseX, mouseY, eventButton);
    }
}
