package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.Minecraft;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class EntityPlayerMPMixin {
//	@Redirect(method = "canCommandSenderUseCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
//	private boolean wide_0() {
//		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
//	}

//	@Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
//	private boolean wide_1() {
//		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
//	}
}
