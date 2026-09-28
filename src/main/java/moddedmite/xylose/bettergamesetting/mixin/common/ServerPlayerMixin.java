package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.util.ChunkTileEntities;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 500))
	private int modifyMaxChunkSize(int constant) {
		return Integer.MAX_VALUE;
	}

	@ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 5, ordinal = 1))
	private int modifyChunkSendBudget(int constant) {
		return 32;
	}

	@ModifyConstant(method = "onUpdate", constant = @Constant(floatValue = 0.8F))
	private float removeServerLoadThrottle(float constant) {
		return Float.MAX_VALUE;
	}

	@ModifyConstant(method = "onUpdate", constant = @Constant(longValue = 10L))
	private long removeChunkSendTimeBudget(long constant) {
		return Long.MAX_VALUE;
	}

	@Redirect(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldServer;getAllTileEntityInBox(IIIIII)Ljava/util/List;"))
	private List replaceTileEntityScan(WorldServer worldServer, int par1, int par2, int par3, int par4, int par5, int par6) {
		return ChunkTileEntities.inBox(worldServer, par1, par2, par3, par4, par5, par6);
	}
}
