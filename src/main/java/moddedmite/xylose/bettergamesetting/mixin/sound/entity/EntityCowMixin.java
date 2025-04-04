package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityCow;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityCow.class)
public class EntityCowMixin {
    /**
     * @author Xy_Lose
     * @reason modify mobs sound volume
     */
    @Overwrite
    protected float getSoundVolume(String sound) {
        if (sound.equals("mob.cow.step")) {
            return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.8f;
        }
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.4f;
    }
}
