package moddedmite.xylose.bettergamesetting.mixin.sound.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Explosion;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @WrapOperation(method = "doExplosionB", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundEffect(DDDLjava/lang/String;FF)V"))
    public void explosionBVolume(World instance, double v, double par1, double par3, String par5, float par7Str, float par8, Operation<Void> original) {
        instance.playSoundEffect(v, par1, par3, par5, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getAmbientVolume() * par7Str, par8);
    }
}
