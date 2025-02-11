package moddedmite.xylose.bettergamesetting.mixin.sound.client.audio;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Entity;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import net.minecraft.SoundUpdaterMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundUpdaterMinecart.class)
public class SoundUpdaterMinecartMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playEntitySound(Ljava/lang/String;Lnet/minecraft/Entity;FFZ)V"))
    public void update(SoundManager instance, String var7, Entity var6, float v, float par1Str, boolean par2Entity, Operation<Void> original) {
        instance.playEntitySound(var7, var6, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * v, par1Str, par2Entity);
    }
}
