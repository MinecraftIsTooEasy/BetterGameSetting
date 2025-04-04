package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityWolf;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityWolf.class)
public abstract class EntityWolfMixin extends EntityLivingBase {
    public EntityWolfMixin(World par1World) {
        super(par1World);
    }

    /**
     * @author Xy_Lose
     * @reason modify mobs sound volume
     */
    @Overwrite
    protected float getSoundVolume(String sound) {
        return  (this.isChild() ? ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.2f : ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.4f);
    }
}
