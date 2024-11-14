package moddedmite.xylose.bettergamesetting.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Entity;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapOperation(method = "moveEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/Entity;playSound(Ljava/lang/String;FF)V"))
    private void moveEntity(Entity instance, String par1Str, float par2, float par3, Operation<Void> original) {
        instance.playSound(par1Str, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getAmbientVolume() * par2, par3);
    }
}
