package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.Minecraft;
import net.minecraft.ServerConfigurationManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerConfigurationManager.class, priority = 10001)
public class ServerConfigurationManagerMixin {
	@Shadow @Final private MinecraftServer mcServer;
	
	@Redirect(method = "setGameType", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_0() {
		return !Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}
	
	@Redirect(method = "setCommandsAllowedForAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_1() {
		return !Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}
	
	@Inject(method = "isPlayerOpped", at = @At("TAIL"), cancellable = true)
	public void wide_2(String par1Str, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BGSConfig.freeDevAllowCheat.get() && this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed());
	}
}
