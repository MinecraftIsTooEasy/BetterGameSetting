package moddedmite.xylose.bettergamesetting.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Entity;
import net.minecraft.EntityPlayer;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin {
    @WrapOperation(method = "updateItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;playSound(Ljava/lang/String;FF)V"))
    private void useItemVolume(EntityPlayer instance, String par1Str, float par2, float par3, Operation<Void> original) {
        instance.playSound(par1Str, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume() * par2, par3);
    }

    @WrapOperation(method = "addExperience(IZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;)V"))
    private void experienceVolume_0(World instance, Entity par1Entity, String par2Str, Operation<Void> original) {
        instance.playSoundAtEntity(par1Entity, par2Str, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume(), 1.0F);
    }

    @WrapOperation(method = "addExperience(IZZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;FF)V"))
    private void experienceVolume_1(World instance, Entity entity, String par1Entity, float par2Str, float par3, Operation<Void> original) {
        instance.playSoundAtEntity(entity, par1Entity, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume() * par2Str, par3);
    }

}
