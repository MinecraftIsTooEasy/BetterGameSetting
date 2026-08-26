package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemMonsterPlacer;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemMonsterPlacer.class, priority = 1001)
public class ItemMonsterPlacerMixin {
	@WrapOperation(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_0(Operation<Boolean> original, @Local(argsOnly = true) EntityPlayer player) {
		return original.call() || (BGSConfig.freeDevAllowCheat.get() && player.getWorld().getWorldInfo().areCommandsAllowed());
	}
}
