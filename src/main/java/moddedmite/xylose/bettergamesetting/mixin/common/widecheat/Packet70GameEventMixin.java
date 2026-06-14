package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.Minecraft;
import net.minecraft.Packet70GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Packet70GameEvent.class, priority = 1001)
public class Packet70GameEventMixin {
	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private static boolean wide_0() {
		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
	}

	@Redirect(method = "<init>(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_1() {
		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
	}
}
