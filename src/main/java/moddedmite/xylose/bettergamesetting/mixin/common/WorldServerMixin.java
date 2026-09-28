package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.ScheduledBlockChange;
import net.minecraft.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldServer.class)
public abstract class WorldServerMixin {
	@Shadow private List scheduled_block_changes;
	@Unique private boolean iteratingScheduledBlockChanges;
	@Unique private final List<ScheduledBlockChange> pendingScheduledBlockChanges = new ArrayList<>();

	@Inject(method = "checkScheduledBlockChanges", at = @At("HEAD"))
	private void beginScheduledBlockChangePass(boolean flush, CallbackInfo ci) {
		this.iteratingScheduledBlockChanges = true;
	}

	@Inject(method = "checkScheduledBlockChanges", at = @At("RETURN"))
	private void endScheduledBlockChangePass(boolean flush, CallbackInfo ci) {
		this.iteratingScheduledBlockChanges = false;
		if (!this.pendingScheduledBlockChanges.isEmpty()) {
			this.scheduled_block_changes.addAll(this.pendingScheduledBlockChanges);
			this.pendingScheduledBlockChanges.clear();
		}
	}

	@Inject(method = "scheduleBlockChange", at = @At("HEAD"), cancellable = true)
	private void deferScheduledBlockChange(int x, int y, int z, int from_block_id, int to_block_id, int to_metadata, int ticks_from_now, CallbackInfo ci) {
		if (this.iteratingScheduledBlockChanges) {
			this.pendingScheduledBlockChanges.add(new ScheduledBlockChange(x, y, z, from_block_id, to_block_id, to_metadata, ticks_from_now));
			ci.cancel();
		}
	}
}
