package moddedmite.xylose.bettergamesetting.mixin.sound.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFlintAndSteel.class)
public class ItemFlintAndSteelMixin {
    @WrapOperation(method = "makeIgniteSoundAndApplyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;FF)V"))
    private void modifyIgniteSound(World instance, Entity entity, String par1Entity, float par2Str, float par3, Operation<Void> original) {
        instance.playSoundAtEntity(entity, par1Entity, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getBlockVolume() * par2Str, par3);
    }

    @WrapOperation(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;FF)V"))
    private void onIgniteRightClickVolume(World instance, Entity entity, String par1Entity, float par2Str, float par3, Operation<Void> original) {
        instance.playSoundAtEntity(entity, par1Entity, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getBlockVolume() * par2Str, par3);
    }
}
