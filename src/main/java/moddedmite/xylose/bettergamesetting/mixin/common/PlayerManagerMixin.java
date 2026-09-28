package moddedmite.xylose.bettergamesetting.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IPlayerInstance;
import moddedmite.xylose.bettergamesetting.api.IPlayerChunkMap;
import moddedmite.xylose.bettergamesetting.mixin.client.invoker.PlayerManagerInvoker;
import net.minecraft.ChunkCoordIntPair;
import net.minecraft.LongHashMap;
import net.minecraft.MathHelper;
import net.minecraft.PlayerInstance;
import net.minecraft.PlayerManager;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements IPlayerChunkMap {
	@Shadow @Final @Mutable private int playerViewRadius;
	@Shadow @Final private List players;
	@Shadow @Final private WorldServer theWorldServer;
	@Shadow @Final private LongHashMap playerInstances;
	@Shadow @Final private List playerInstanceList;
	@Shadow @Final private List chunkWatcherWithPlayers;
	@Shadow protected abstract PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3);
	@Shadow protected abstract boolean overlaps(int par1, int par2, int par3, int par4, int par5);

	@Unique private final List<PlayerInstance> pendingChunkLoads = new ArrayList<>();
	@Unique private int pendingChunkLoadSortTimer;
	@Unique private List filteredLoadedChunks;
	@Unique private Set filteredLoadedChunkLookup;

	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 15))
	private int modifyMaxRadius(int original) {
		return 32;
	}

	@Override
	public void queueChunkLoad(PlayerInstance instance) {
		this.pendingChunkLoads.add(instance);
		this.pendingChunkLoadSortTimer = 0;
	}

	@Override
	public void removeChunkWatcher(PlayerInstance instance, int x, int z, boolean hasPendingUpdates) {
		this.playerInstances.remove((long) x + Integer.MAX_VALUE | (long) z + Integer.MAX_VALUE << 32);
		this.playerInstanceList.remove(instance);
		if (hasPendingUpdates) {
			this.chunkWatcherWithPlayers.remove(instance);
		}
	}

	@Unique
	private double closestPlayerDistanceSq(PlayerInstance instance) {
		ChunkCoordIntPair location = ((IPlayerInstance) instance).getChunkLocation();
		double closest = Double.MAX_VALUE;
		for (Object object : instance.playersInChunk) {
			ServerPlayer player = (ServerPlayer) object;
			double dx = (double) location.getCenterXPos() - player.posX;
			double dz = (double) location.getCenterZPosition() - player.posZ;
			double distanceSq = dx * dx + dz * dz;
			if (distanceSq < closest) {
				closest = distanceSq;
			}
		}
		return closest;
	}

	@Inject(method = "updatePlayerInstances", at = @At("HEAD"))
	private void tickPendingChunkLoads(CallbackInfo ci) {
		if (this.pendingChunkLoads.isEmpty()) return;
		if (--this.pendingChunkLoadSortTimer <= 0) {
			this.pendingChunkLoads.sort((a, b) -> Double.compare(this.closestPlayerDistanceSq(a), this.closestPlayerDistanceSq(b)));
			this.pendingChunkLoadSortTimer = 4;
		}
		long deadline = System.nanoTime() + 50000000L;
		int budget = Integer.MAX_VALUE;
		Iterator<PlayerInstance> iterator = this.pendingChunkLoads.iterator();
		while (iterator.hasNext()) {
			PlayerInstance instance = iterator.next();
			iterator.remove();
			if (instance.playersInChunk.isEmpty()) {
				continue;
			}
			ChunkCoordIntPair location = ((IPlayerInstance) instance).getChunkLocation();
			this.theWorldServer.theChunkProviderServer.loadChunk(location.chunkXPos, location.chunkZPos);
			if (--budget < 0 || System.nanoTime() > deadline) {
				break;
			}
		}
	}

	@WrapOperation(method = "filterChunkLoadQueue", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;contains(Ljava/lang/Object;)Z"))
	private boolean lookupLoadedChunk(ArrayList instance, Object o, Operation<Boolean> original) {
		if (this.filteredLoadedChunks != instance) {
			this.filteredLoadedChunks = instance;
			this.filteredLoadedChunkLookup = new HashSet(instance);
		}
		return this.filteredLoadedChunkLookup.contains(o);
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
								PlayerInstance playerinstance = this.getOrCreateChunkWatcher(i1, j1, false);
								if (playerinstance != null) {
									playerinstance.removePlayer(player);
								}
							}
						}
					}
				}
			}

			this.playerViewRadius = viewDistance;
		}
	}
}
