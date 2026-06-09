package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.mixin.client.invoker.ServerConfigurationManagerInvoker;
import net.minecraft.DedicatedPlayerList;
import net.minecraft.DedicatedServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedPlayerList.class)
public class DedicatedPlayerListMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInitTail(DedicatedServer par1DedicatedServer, CallbackInfo ci) {
		((ServerConfigurationManagerInvoker) ReflectHelper.dyCast(this)).invokerParseDistance(par1DedicatedServer.getIntProperty("view-distance", 10));
	}
}
