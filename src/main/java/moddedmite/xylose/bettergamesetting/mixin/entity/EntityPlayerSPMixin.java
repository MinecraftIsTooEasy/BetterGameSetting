package moddedmite.xylose.bettergamesetting.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.ClientPlayer;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayer.class)
public class EntityPlayerSPMixin {
    @WrapOperation(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSoundFX(Ljava/lang/String;FF)V"))
    public void onLivingUpdate(SoundManager instance, String var4, float v, float par1Str, Operation<Void> original) {
        instance.playSoundFX(var4, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getAmbientVolume() * v, par1Str);
    }
}
