package moddedmite.xylose.bettergamesetting.mixin.common;

import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 500))
	private int modifyMaxChunkSize(int constant) {
		return Integer.MAX_VALUE;
	}
}
