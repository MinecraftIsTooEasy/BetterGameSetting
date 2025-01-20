package moddedmite.xylose.bettergamesetting.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.IWorldAccess;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(World.class)
public class WorldMixin {
    @WrapOperation(method = "playSoundAtBlock(IIILjava/lang/String;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/IWorldAccess;playSound(Ljava/lang/String;DDDFF)V"))
    public void playSoundAtBlock(IWorldAccess instance, String s, double x, double y, double z, float volume, float pitch, Operation<Void> original) {
        instance.playSound(s, x, y, z, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getBlockVolume() * volume, pitch);
    }

    @WrapOperation(method = "moodSoundAndLightCheck", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSoundFX(Ljava/lang/String;FF)V"))
    public void moodSoundAndLightCheck(SoundManager instance, String var4, float v, float par1Str, Operation<Void> original) {
        instance.playSoundFX(var4, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getBlockVolume() * v, par1Str);
    }
}
