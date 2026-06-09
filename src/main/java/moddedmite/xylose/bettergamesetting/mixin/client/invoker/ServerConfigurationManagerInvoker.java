package moddedmite.xylose.bettergamesetting.mixin.client.invoker;

import net.minecraft.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerConfigurationManager.class)
public interface ServerConfigurationManagerInvoker {
	@Invoker("parseDistance(I)V")
	void invokerParseDistance(int viewDistance);
}
