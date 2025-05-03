package moddedmite.xylose.bettergamesetting.mixin.sound.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.ItemFireball;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFireball.class)
public class ItemFireballMixin {
    @WrapOperation(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSoundAtBlock(IIILjava/lang/String;FF)V"))
    private void onFireballRightClickVolume(World instance, int i, int x, int y, String z, float name, float volume, Operation<Void> original) {
        instance.playSoundAtBlock(i, x, y, z, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getBlockVolume() * name, volume);
    }
}
