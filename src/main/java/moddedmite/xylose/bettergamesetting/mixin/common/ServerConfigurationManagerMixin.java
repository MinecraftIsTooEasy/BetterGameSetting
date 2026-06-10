package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.mixin.client.invoker.PlayerManagerInvoker;
import moddedmite.xylose.bettergamesetting.mixin.client.invoker.ServerConfigurationManagerInvoker;
import net.minecraft.ServerConfigurationManager;
import net.minecraft.WorldServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
	@Shadow @Final private MinecraftServer mcServer;
	@Shadow protected int viewDistance;
	
	/**
	 * {@link ServerConfigurationManagerInvoker#invokerParseDistance(int)}
	 */
	@Unique
	public void parseDistance(int distance) {
		this.viewDistance = distance;
		if (this.mcServer.worldServers != null) {
			WorldServer[] aworldserver = this.mcServer.worldServers;
			for (WorldServer worldserver : aworldserver) {
				if (worldserver != null) {
					((PlayerManagerInvoker) worldserver.getPlayerManager()).invokerResetViewRadius(distance);
				}
			}
		}
	}
}
