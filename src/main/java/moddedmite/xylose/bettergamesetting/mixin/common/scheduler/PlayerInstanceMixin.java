package moddedmite.xylose.bettergamesetting.mixin.common.scheduler;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IPlayerChunkMap;
import moddedmite.xylose.bettergamesetting.compat.ModCompat;
import net.minecraft.Chunk;
import net.minecraft.ChunkProviderServer;
import net.minecraft.PlayerInstance;
import net.minecraft.PlayerManager;
import net.minecraft.ServerPlayer;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInstance.class)
public abstract class PlayerInstanceMixin {
    @Shadow @Final PlayerManager thePlayerManager;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/ChunkProviderServer;loadChunk(II)Lnet/minecraft/Chunk;"))
    private Chunk deferChunkLoad(ChunkProviderServer instance, int chunkX, int chunkZ, Operation<Chunk> original) {
        if (!ModCompat.isServerSlowChunkLoadingEnabled(this.thePlayerManager.getWorldServer().getMinecraftServer())) {
            return original.call(instance, chunkX, chunkZ);
        }
        return null;
    }

    @Inject(method = "addPlayer", at = @At("TAIL"))
    private void queueChunkLoad(ServerPlayer player, CallbackInfo ci) {
        if (!ModCompat.isServerSlowChunkLoadingEnabled(this.thePlayerManager.getWorldServer().getMinecraftServer())) {
            return;
        }
        ((IPlayerChunkMap) this.thePlayerManager).queueChunkLoad(ReflectHelper.dyCast(this));
    }
}
