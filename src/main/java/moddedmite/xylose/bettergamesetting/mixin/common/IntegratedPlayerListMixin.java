package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.mixin.client.invoker.ServerConfigurationManagerInvoker;
import net.minecraft.IntegratedPlayerList;
import net.minecraft.IntegratedServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedPlayerList.class)
public class IntegratedPlayerListMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void onInitTail(IntegratedServer par1, CallbackInfo ci) {
		((ServerConfigurationManagerInvoker) ReflectHelper.dyCast(this)).invokerParseDistance(10);
	}
}
