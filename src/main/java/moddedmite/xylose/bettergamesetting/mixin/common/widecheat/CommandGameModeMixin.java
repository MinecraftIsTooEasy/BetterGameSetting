package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.CommandGameMode;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CommandGameMode.class, priority = 1001)
public class CommandGameModeMixin {
	@WrapOperation(method = "processCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_0(Operation<Boolean> original) {
		return !original.call() || !BGSConfig.freeDevAllowCheat.get();
	}
}
