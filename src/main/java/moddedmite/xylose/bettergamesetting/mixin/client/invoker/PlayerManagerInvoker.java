package moddedmite.xylose.bettergamesetting.mixin.client.invoker;

import net.minecraft.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerManager.class)
public interface PlayerManagerInvoker {
	@Invoker("resetViewRadius(I)V")
	void invokerResetViewRadius(int viewDistance);
}
