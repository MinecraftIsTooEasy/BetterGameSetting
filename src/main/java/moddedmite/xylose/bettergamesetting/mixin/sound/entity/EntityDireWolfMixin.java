package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityDireWolf;
import net.minecraft.EntityLivingBase;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityDireWolf.class)
public abstract class EntityDireWolfMixin extends EntityLivingBase {
    public EntityDireWolfMixin(World par1World) {
        super(par1World);
    }

    @Overwrite
    protected float getSoundVolume(String sound) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * (super.getSoundVolume(sound) * 1.5f);
    }
}
