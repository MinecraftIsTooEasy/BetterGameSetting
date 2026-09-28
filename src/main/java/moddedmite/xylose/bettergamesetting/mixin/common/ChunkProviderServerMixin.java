package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.ChunkProviderServer;
import net.minecraft.LongHashMap;
import net.minecraft.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ChunkProviderServer.class)
public abstract class ChunkProviderServerMixin {
	@Shadow private Set chunksToUnload;
	@Shadow public LongHashMap loadedChunkHashMap;
	@Shadow public WorldServer worldObj;

	@Inject(method = "unloadQueuedChunks", at = @At("HEAD"))
	private void discardUnloadRequestsForMissingChunks(CallbackInfoReturnable<Boolean> cir) {
		if (this.worldObj.canNotSave || this.chunksToUnload.isEmpty()) return;
		this.chunksToUnload.removeIf(o -> this.loadedChunkHashMap.getValueByKey((Long) o) == null);
	}
}
