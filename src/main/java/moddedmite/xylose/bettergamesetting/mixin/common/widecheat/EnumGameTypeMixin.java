package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.EnumGameType;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnumGameType.class, priority = 1001)
public class EnumGameTypeMixin {

	@Redirect(method = "getByName", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private static boolean wide_1() {
		return !Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}

	@Redirect(method = "isCreative", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_2() {
		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
	}

	@Redirect(method = "isSurvivalOrAdventure", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_3() {
		return !Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
	}
	
//	@Restriction(conflict = @Condition("ohmycommands"))
	@Mixin(EnumGameType.class)
	public static class OhMyCommandsCompatMixin {
		@Redirect(method = "getByID", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
		private static boolean wide_0() {
			return !Minecraft.inDevMode() || !BGSConfig.freeDevAllowCheat.get();
		}
	}
}
