package moddedmite.xylose.bettergamesetting.mixin.sound.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Entity;
import net.minecraft.ItemBlock;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemBlock.class)
public class ItemBlockMixin {
    @WrapOperation(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;FF)V"))
    private void modifyVolume(World instance, Entity entity, String par1Entity, float par2Str, float par3, Operation<Void> original) {
        instance.playSoundAtEntity(entity, par1Entity, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getPlayerVolume() * par2Str, par3);
    }
}
