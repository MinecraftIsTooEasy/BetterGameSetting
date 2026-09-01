package moddedmite.xylose.bettergamesetting.mixin.client.audio;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.BlockInfo;
import net.minecraft.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;makeSound(Ljava/lang/String;FF)V"), index = 0)
	private String stepSoundFix(String sound, @Local(name = "block_landed_on_info") BlockInfo block_landed_on_info) {
		return block_landed_on_info.block.stepSound.getStepSound();
	}
}
