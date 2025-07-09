package moddedmite.xylose.bettergamesetting.mixin.sound.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.GuiSlotStats;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiSlotStats.class)
public class GuiSlotStatsMixin {
    @ModifyArg(method = "func_77224_a", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSoundFX(Ljava/lang/String;FF)V"))
    private float modifyUIVolume(float volume) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getUIVolume();
    }
}
