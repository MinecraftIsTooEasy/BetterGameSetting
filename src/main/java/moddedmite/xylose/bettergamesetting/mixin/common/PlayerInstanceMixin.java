package moddedmite.xylose.bettergamesetting.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IPlayerInstance;
import moddedmite.xylose.bettergamesetting.api.IPlayerChunkMap;
import moddedmite.xylose.bettergamesetting.util.ChunkTileEntities;
import net.minecraft.Chunk;
import net.minecraft.ChunkCoordIntPair;
import net.minecraft.ChunkProviderServer;
import net.minecraft.PlayerInstance;
import net.minecraft.PlayerManager;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerInstance.class)
public abstract class PlayerInstanceMixin implements IPlayerInstance {
	@Shadow @Final public List playersInChunk;
	@Shadow @Final private ChunkCoordIntPair chunkLocation;
	@Shadow @Final PlayerManager thePlayerManager;
	@Shadow private int numberOfTilesToUpdate;

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/ChunkProviderServer;loadChunk(II)Lnet/minecraft/Chunk;"))
	private Chunk deferChunkLoad(ChunkProviderServer instance, int var8, int var9, Operation<Chunk> original) {
		return null;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void queueChunkLoad(CallbackInfo ci) {
		((IPlayerChunkMap) this.thePlayerManager).queueChunkLoad(ReflectHelper.dyCast(this));
	}

	@Override
	public ChunkCoordIntPair getChunkLocation() {
		return this.chunkLocation;
	}

	@WrapOperation(method = "sendChunkUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldServer;getAllTileEntityInBox(IIIIII)Ljava/util/List;"))
	private List replaceTileEntityScan(WorldServer instance, int var8, int i, int par1, int par2, int par3, int par4, Operation<List> original) {
		return ChunkTileEntities.inBox(instance, var8, i, par1, par2, par3, par4);
	}

	@WrapOperation(method = "sendChunkUpdateMITE", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldServer;getAllTileEntityInBox(IIIIII)Ljava/util/List;"))
	private List replaceTileEntityScanMITE(WorldServer instance, int var8, int i, int par1, int par2, int par3, int par4, Operation<List> original) {
		return ChunkTileEntities.inBox(instance, var8, i, par1, par2, par3, par4);
	}

	@Inject(method = "removePlayer", at = @At("HEAD"), cancellable = true)
	private void skipGenerateForMissingChunk(ServerPlayer par1EntityPlayerMP, CallbackInfo ci) {
		if (!this.playersInChunk.contains(par1EntityPlayerMP)) return;
		int x = this.chunkLocation.chunkXPos;
		int z = this.chunkLocation.chunkZPos;
		WorldServer worldServer = this.thePlayerManager.getWorldServer();
		if (worldServer.theChunkProviderServer.getChunkIfItExists(x, z) != null) return;
		this.playersInChunk.remove(par1EntityPlayerMP);
		par1EntityPlayerMP.loadedChunks.remove(this.chunkLocation);
		if (this.playersInChunk.isEmpty()) {
			((IPlayerChunkMap) this.thePlayerManager).removeChunkWatcher(ReflectHelper.dyCast(this), x, z, this.numberOfTilesToUpdate > 0);
			worldServer.theChunkProviderServer.unloadChunksIfNotNearSpawn(x, z);
		}
		ci.cancel();
	}
}
