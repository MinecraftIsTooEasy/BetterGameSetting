package moddedmite.xylose.bettergamesetting.mixin.sound.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.GuiButtonNextPage;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiButtonNextPage.class)
public class GuiButtonNextPageMixin {
    @ModifyArg(method = "<init>", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButtonNextPage;setClickedSound(Ljava/lang/String;FF)Lnet/minecraft/GuiButton;"))
    private float modifyUIVolume(float volume) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getUIVolume();
    }
}
