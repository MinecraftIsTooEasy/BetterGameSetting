package moddedmite.xylose.bettergamesetting.mixin.compat.neodymium;

import makamys.neodymium.renderer.NeoChunk;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = @Condition("neodymium"))
@Mixin(NeoChunk.class)
public class NeoChunkMixin {
	
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 32))
	private int modifyChunkMeshArraySize(int constant) {
		return 256;
	}
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 16))
	private int modifySectionVisibleArraySize(int constant) {
		return 256;
	}
}
