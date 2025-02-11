package moddedmite.xylose.bettergamesetting.mixin.sound.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Minecraft;
import net.minecraft.PlayerControllerMP;
import net.minecraft.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Shadow @Final private Minecraft mc;

    @WrapOperation(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSound(Ljava/lang/String;FFFFF)V"))
    public void onPlayerDamageBlock(SoundManager instance, String var9, float var7, float v, float par1Str, float par2, float par3, Operation<Void> original) {
        this.mc.sndManager.playSound(var9, var7, v, par1Str, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par2, par3);
    }
}
