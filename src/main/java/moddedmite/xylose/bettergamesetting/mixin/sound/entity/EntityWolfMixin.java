package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityWolf;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityWolf.class)
public abstract class EntityWolfMixin extends EntityLivingBase {
    public EntityWolfMixin(World par1World) {
        super(par1World);
    }

    @ModifyReturnValue(method = "getSoundVolume", at = @At("TAIL"))
    protected float wolfVolume(float original) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * original;
    }
}
