package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(GuiSlotLanguage.class)
public abstract class GuiSlotLanguageMixin extends GuiSlot {
    @Shadow @Final private List<String> field_77251_g;
    @Shadow @Final private Map<String, Language> field_77253_h;

    @Unique private List<String> filteredLanguages = new ArrayList<>();

    public GuiSlotLanguageMixin(Minecraft par1Minecraft, int par2, int par3, int par4, int par5, int par6) {
        super(par1Minecraft, par2, par3, par4, par5, par6);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(GuiLanguage guiLanguage, CallbackInfo ci) {
        this.filteredLanguages.addAll(this.field_77251_g);
    }

    /**
     * {@link moddedmite.xylose.bettergamesetting.mixin.client.invoker.GuiSlotLanguageInvoker#invokeUpdateFilteredLanguages(String)}
     */
    @Unique
    private void updateFilteredLanguages(String filterText) {
        this.filteredLanguages.clear();

        if (filterText.isEmpty()) {
            this.filteredLanguages.addAll(this.field_77251_g);
        } else {
            String lowerCaseFilter = filterText.toLowerCase();

            for (String langCode : this.field_77251_g) {
                Language lang = this.field_77253_h.get(langCode);
                if (lang != null && (lang.getLanguageCode().toLowerCase().contains(lowerCaseFilter) ||
                        lang.toString().toLowerCase().contains(lowerCaseFilter))) {
                    this.filteredLanguages.add(langCode);
                }
            }
        }
    }

    @Redirect(method = "getSize", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiSlotLanguage;field_77251_g:Ljava/util/List;"))
    private List<String> redirectGetSize(GuiSlotLanguage instance) {
        return this.filteredLanguages;
    }

    @Redirect(method = "elementClicked", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiSlotLanguage;field_77251_g:Ljava/util/List;"))
    private List<String> redirectElementClicked(GuiSlotLanguage instance) {
        return this.filteredLanguages;
    }

    @Redirect(method = "isSelected", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiSlotLanguage;field_77251_g:Ljava/util/List;"))
    private List<String> redirectIsSelected(GuiSlotLanguage instance, int index) {
        return this.filteredLanguages;
    }

    @Redirect(method = "drawSlot", at = @At(value = "FIELD", target = "Lnet/minecraft/GuiSlotLanguage;field_77251_g:Ljava/util/List;"))
    private List<String> redirectDrawSlot(GuiSlotLanguage instance, int index) {
        return this.filteredLanguages;
    }
}
