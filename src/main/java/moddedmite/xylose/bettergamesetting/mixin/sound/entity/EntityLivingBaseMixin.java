package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
    @WrapOperation(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;makeSound(Ljava/lang/String;)V"))
    private void fallVolume(EntityLivingBase instance, String sound, Operation<Void> original) {
        instance.makeSound(sound, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume(), 1.0F);
    }
    @WrapOperation(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;makeSound(Ljava/lang/String;FF)V", ordinal = 0))
    private void stepVolume(EntityLivingBase instance, String sound, float volume_multiplier, float pitch_multiplier, Operation<Void> original) {
        instance.makeSound(sound, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume() * volume_multiplier, pitch_multiplier);
    }
    @WrapOperation(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;makeSound(Ljava/lang/String;)V"))
    private void damageVolume(EntityLivingBase instance, String sound, Operation<Void> original) {
        if (instance instanceof EntityPlayer) {
            instance.makeSound(sound, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume(), 1.0F);
        } else {
            original.call(instance, sound);
        }
    }
    /**
     * @author Xy_Lose
     * @reason modify mobs sound volume
     */
    @Overwrite
    protected float getSoundVolume(String sound) {
        if ((ReflectHelper.dyCast(this) instanceof IMob) || (ReflectHelper.dyCast(this) instanceof EntityMob)) {
            return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getHostileVolume();
        } else if (!(ReflectHelper.dyCast(this) instanceof EntityPlayer)) {
            return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume();
        }
        return 1.0F;
    }

}
