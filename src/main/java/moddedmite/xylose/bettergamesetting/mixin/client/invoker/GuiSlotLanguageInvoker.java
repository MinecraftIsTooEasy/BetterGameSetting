package moddedmite.xylose.bettergamesetting.mixin.client.invoker;

import net.minecraft.GuiSlotLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiSlotLanguage.class)
public interface GuiSlotLanguageInvoker {
    @Invoker("updateFilteredLanguages")
    void invokeUpdateFilteredLanguages(String filterText);
}
