package moddedmite.xylose.bettergamesetting.mixin.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityWoodSpider;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityWoodSpider.class)
public abstract class EntityWoodSpiderMixin extends EntityLivingBase {
    public EntityWoodSpiderMixin(World par1World) {
        super(par1World);
    }

    @Overwrite
    protected float getSoundVolume(String sound) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getHostileVolume() * (super.getSoundVolume(sound) * 0.6f);
    }
}
