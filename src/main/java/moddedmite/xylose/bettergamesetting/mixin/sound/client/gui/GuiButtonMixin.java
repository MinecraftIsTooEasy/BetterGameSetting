package moddedmite.xylose.bettergamesetting.mixin.sound.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Gui;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiButton.class)
public class GuiButtonMixin extends Gui {
    @Shadow private float clicked_sound_volume;

//    @Inject(method = "setClickedSound", at = @At("TAIL"))
//    private void modifyUIVolume(String clicked_sound, float volume, float pitch, CallbackInfoReturnable<GuiButton> cir) {
//        this.clicked_sound_volume = ((IGameSetting) Minecraft.getMinecraft().gameSettings).getUIVolume();
//    }
}
