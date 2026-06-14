package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.ItemInWorldManager;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ItemInWorldManager.class, priority = 1001)
public class ItemInWorldManagerMixin {

	@WrapOperation(method = "initializeGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_1(Operation<Boolean> original) {
		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
	}

	@Mixin(ItemInWorldManager.class)
	public static class OhMyCommandsCompatMixin {
		@WrapOperation(method = "getGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
		private boolean wide_0(Operation<Boolean> original) {
			return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
		}

		@WrapOperation(method = "isCreative", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
		private boolean wide_2(Operation<Boolean> original) {
			return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
		}
		
		@WrapOperation(method = "setGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
		private boolean wide_3(Operation<Boolean> original) {
			return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
		}
	}
}
