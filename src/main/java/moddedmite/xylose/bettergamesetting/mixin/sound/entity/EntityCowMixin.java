package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityCow;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityCow.class)
public class EntityCowMixin {

    @ModifyReturnValue(method = "getSoundVolume", at = @At("TAIL"))
    protected float cowVolume(float original, @Local(name = "sound") String sound) {
        if (sound.equals("mob.cow.step")) {
            return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.8f;
        }
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * 0.4f;
    }
}
