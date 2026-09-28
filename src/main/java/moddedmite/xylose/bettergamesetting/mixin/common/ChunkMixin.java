package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Shadow @Final @Mutable public boolean[] pending_skylight_updates;
    @Shadow @Final @Mutable public boolean[] pending_blocklight_updates;
    @Shadow public int num_pending_skylight_updates;
    @Shadow public int num_pending_blocklight_updates;

    @Unique private static final int lightUpdateTableSize = 65536;
    @Unique private static final boolean[] emptyLightUpdateTable = new boolean[0];

    @Inject(method = "addPendingSkylightUpdate", at = @At("HEAD"))
    private void allocateSkylightUpdateTable(int x, int y, int z, CallbackInfo ci) {
        if (this.pending_skylight_updates == null || this.pending_skylight_updates.length != lightUpdateTableSize) {
            this.pending_skylight_updates = new boolean[lightUpdateTableSize];
        }
    }

    @Inject(method = "addPendingBlocklightUpdate", at = @At("HEAD"))
    private void allocateBlocklightUpdateTable(int x, int y, int z, CallbackInfo ci) {
        if (this.pending_blocklight_updates == null || this.pending_blocklight_updates.length != lightUpdateTableSize) {
            this.pending_blocklight_updates = new boolean[lightUpdateTableSize];
        }
    }

    @Inject(method = "performPendingSkylightUpdatesIfPossible", at = @At("RETURN"))
    private void releaseSkylightUpdateTable(CallbackInfoReturnable<Boolean> cir) {
        if (this.num_pending_skylight_updates == 0 && this.pending_skylight_updates != null && this.pending_skylight_updates.length == lightUpdateTableSize) {
            this.pending_skylight_updates = emptyLightUpdateTable;
        }
    }

    @Inject(method = "performPendingBlocklightUpdatesIfPossible", at = @At("RETURN"))
    private void releaseBlocklightUpdateTable(CallbackInfoReturnable<Boolean> cir) {
        if (this.num_pending_blocklight_updates == 0 && this.pending_blocklight_updates != null && this.pending_blocklight_updates.length == lightUpdateTableSize) {
            this.pending_blocklight_updates = emptyLightUpdateTable;
        }
    }
}
