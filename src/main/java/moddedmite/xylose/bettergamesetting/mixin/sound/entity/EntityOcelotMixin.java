package moddedmite.xylose.bettergamesetting.mixin.sound.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityOcelot;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityOcelot.class)
public class EntityOcelotMixin {
    @ModifyReturnValue(method = "getSoundVolume", at = @At("TAIL"))
    protected float ocelotVolume(float original) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getNeutralVolume() * original;
    }
}
