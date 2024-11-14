package moddedmite.xylose.bettergamesetting.mixin.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityBat;
import net.minecraft.EntityLivingBase;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityBat.class)
public abstract class EntityBatMixin extends EntityLivingBase {
    @Shadow public abstract float getScaleFactor();

    public EntityBatMixin(World par1World) {
        super(par1World);
    }

    @Overwrite
    protected float getSoundVolume(String sound) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * (super.getSoundVolume(sound) * 0.1f * this.getScaleFactor());
    }
}
