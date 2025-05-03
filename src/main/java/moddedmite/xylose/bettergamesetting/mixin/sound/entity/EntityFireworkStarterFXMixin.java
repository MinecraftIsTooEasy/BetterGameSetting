package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityFireworkStarterFX;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityFireworkStarterFX.class)
public class EntityFireworkStarterFXMixin {
    @WrapOperation(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSound(DDDLjava/lang/String;FFZ)V"))
    private void fireworkStarterFXVolume(World instance, double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10, Operation<Void> original) {
        instance.playSound(par1, par3, par5, par7Str, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getAmbientVolume() * par8, par9, par10);
    }
}
