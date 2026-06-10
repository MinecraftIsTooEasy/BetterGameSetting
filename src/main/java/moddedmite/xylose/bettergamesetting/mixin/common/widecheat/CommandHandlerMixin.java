package moddedmite.xylose.bettergamesetting.mixin.common.widecheat;

import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.CommandHandler;
import net.minecraft.EntityPlayer;
import net.minecraft.ICommandSender;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CommandHandler.class, priority = 1001)
public class CommandHandlerMixin {
	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 1))
	private boolean wide_0(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 2))
	private boolean wide_1(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 3))
	private boolean wide_2(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 4))
	private boolean wide_3(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 5))
	private boolean wide_4(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 6))
	private boolean wide_5(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 7))
	private boolean wide_6(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Redirect(method = "executeCommand", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z", ordinal = 8))
	private boolean wide_7(@Local(argsOnly = true) ICommandSender par1ICommandSender) {
		return Minecraft.inDevMode() || (BGSConfig.freeDevAllowCheat.get() && par1ICommandSender.getEntityWorld().getWorldInfo().areCommandsAllowed());
	}

	@Inject(method = "isUserPrivileged", at = @At("TAIL"), cancellable = true)
	private void wide_8(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(BGSConfig.freeDevAllowCheat.get() && player.getWorld().getWorldInfo().areCommandsAllowed());
	}
}
