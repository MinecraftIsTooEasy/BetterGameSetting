package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.mixin.client.invoker.ServerConfigurationManagerInvoker;
import net.minecraft.IntegratedServer;
import net.minecraft.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServer {
	@Shadow @Final private Minecraft mc;

	public IntegratedServerMixin(File par1File) {
		super(par1File);
	}
	
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tick()V", shift = At.Shift.AFTER))
	private void onTick(CallbackInfo ci) {
		if (this.mc.gameSettings.getRenderDistance() != this.getConfigurationManager().getViewDistance()) {
			((ServerConfigurationManagerInvoker) this.getConfigurationManager()).invokerParseDistance(this.mc.gameSettings.getRenderDistance());
		}
	}
}
