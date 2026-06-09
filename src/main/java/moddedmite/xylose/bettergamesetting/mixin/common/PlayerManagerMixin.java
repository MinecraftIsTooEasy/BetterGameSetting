package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.mixin.client.invoker.PlayerManagerInvoker;
import net.minecraft.MathHelper;
import net.minecraft.PlayerInstance;
import net.minecraft.PlayerManager;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Shadow @Final @Mutable private int playerViewRadius;
	@Shadow @Final private List players;
	@Shadow protected abstract PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3);
	@Shadow protected abstract boolean overlaps(int par1, int par2, int par3, int par4, int par5);
	
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 15))
	private int modifyMaxRadius(int original) {
		return 32;
	}

	/**
	 * {@link PlayerManagerInvoker#invokerResetViewRadius(int)}
	 */
	public void resetViewRadius(int viewDistance) {
		viewDistance = MathHelper.clamp_int(viewDistance, 3, 32);
		if (viewDistance != this.playerViewRadius) {
			int j = viewDistance - this.playerViewRadius;
			for (ServerPlayer player : (List<ServerPlayer>) this.players) {
				int k = (int) player.posX >> 4;
				int l = (int) player.posZ >> 4;
				int i1;
				int j1;
				if (j > 0) {
					for (i1 = k - viewDistance; i1 <= k + viewDistance; ++i1) {
						for (j1 = l - viewDistance; j1 <= l + viewDistance; ++j1) {
							PlayerInstance playerinstance = this.getOrCreateChunkWatcher(i1, j1, true);
							if (!playerinstance.playersInChunk.contains(player)) {
								playerinstance.addPlayer(player);
							}
						}
					}
				} else {
					for (i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
						for (j1 = l - this.playerViewRadius; j1 <= l + this.playerViewRadius; ++j1) {
							if (!this.overlaps(i1, j1, k, l, viewDistance)) {
								this.getOrCreateChunkWatcher(i1, j1, true).removePlayer(player);
							}
						}
					}
				}
			}
			
			this.playerViewRadius = viewDistance;
		}
	}
}
