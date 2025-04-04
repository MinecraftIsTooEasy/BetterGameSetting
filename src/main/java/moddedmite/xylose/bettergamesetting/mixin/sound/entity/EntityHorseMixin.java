package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityHorse;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityHorse.class)
public class EntityHorseMixin {
    /**
     * @author Xy_Lose
     * @reason modify mobs sound volume
     */
    @Overwrite
    protected float getSoundVolume(String sound) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.8f;
    }
}
