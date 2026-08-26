package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.Minecraft;
import net.minecraft.WorldInfo;
import net.minecraft.WorldInfoShared;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = WorldInfo.class, priority = 1001)
public class WorldInfoMixin {
	@Shadow private WorldInfoShared shared;
	
	@ModifyReturnValue(method = "areCommandsAllowed", at = @At("RETURN"))
	public boolean wide_0(boolean original) {
		return original || (BGSConfig.freeDevAllowCheat.get() && shared.allowCommands);
	}
	
	@WrapOperation(method = "getGameType()Lnet/minecraft/EnumGameType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_1(Operation<Boolean> original) {
		return original.call() || BGSConfig.freeDevAllowCheat.get();
	}

	@WrapOperation(method = "setGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_2(Operation<Boolean> original) {
		return original.call() || BGSConfig.freeDevAllowCheat.get();
	}
}
