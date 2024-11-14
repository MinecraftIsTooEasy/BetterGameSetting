package moddedmite.xylose.bettergamesetting.mixin.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityWitch;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityWitch.class)
public class EntityWitchMixin {
    @Overwrite
    protected float getSoundVolume(String sound) {
        if (sound.equals("imported.mob.witch.cackle")) {
            return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getHostileVolume() * 0.6f;
        }
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getHostileVolume() * 0.2f;
    }
}
