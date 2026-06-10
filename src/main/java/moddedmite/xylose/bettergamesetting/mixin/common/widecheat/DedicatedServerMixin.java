package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.DedicatedServer;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DedicatedServer.class, priority = 1001)
public class DedicatedServerMixin {
	@Redirect(method = "getGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_0() {
		return Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}

//	@Redirect(method = "getRequiredPyramidHeight", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
//	private static boolean wide_1() {
//		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
//	}

	@Redirect(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 1))
	private boolean wide_2() {
		return Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}
}
