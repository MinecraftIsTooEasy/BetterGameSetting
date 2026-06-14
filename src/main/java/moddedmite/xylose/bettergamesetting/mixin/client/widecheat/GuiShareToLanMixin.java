package moddedmite.xylose.bettergamesetting.mixin.client.widecheat;

import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.ChatMessageComponent;
import net.minecraft.EnumGameType;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiShareToLan;
import net.minecraft.IntegratedServer;
import net.minecraft.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiShareToLan.class, priority = 1001)
public abstract class GuiShareToLanMixin extends GuiScreen {
	@Shadow private GuiButton buttonAllowCommandsToggle;
	@Shadow private GuiButton buttonGameMode;
	@Shadow private String gameMode;
	@Shadow private boolean allowCommands;
	@Shadow public static void shareToLAN() {}
	
	@Redirect(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
	private boolean wide_0() {
		return Minecraft.inDevMode() || BGSConfig.freeDevAllowCheat.get();
	}

	@Inject(method = "initGui", at = @At("TAIL"))
	private void wide_1(CallbackInfo ci) {
		if (!BGSConfig.freeDevAllowCheat.get()) return;
		this.buttonAllowCommandsToggle.enabled = true;
		this.buttonGameMode.enabled = true;
	}

	@ModifyConstant(method = "actionPerformed", constant = @Constant(stringValue = "survival"))
	private String modifyGameMode(String original) {
		if (!BGSConfig.freeDevAllowCheat.get()) return original;
		if (this.gameMode.equals("survival")) {
			return "creative";
		} else if (this.gameMode.equals("creative")) {
			return "adventure";
		} else {
			return "survival";
		}
	}
	@Redirect(
		method = "actionPerformed",
		at = @At(
				value = "FIELD",
				target = "Lnet/minecraft/GuiShareToLan;allowCommands:Z",
				opcode = Opcodes.PUTFIELD,
				ordinal = 1
		)
	)
	private void redirectSecondAssignment(GuiShareToLan instance, boolean value) {
	}

	@Redirect(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiShareToLan;shareToLAN()V"))
	private void wide_4() {
		if (!BGSConfig.freeDevAllowCheat.get()) shareToLAN();
		String s = this.mc.getIntegratedServer().shareToLAN(EnumGameType.getByName(this.gameMode), this.allowCommands);
		ChatMessageComponent message;
		if (s != null) {
			message = ChatMessageComponent.createFromTranslationWithSubstitutions("commands.publish.started", s);
		} else {
			message = ChatMessageComponent.createFromTranslationKey("commands.publish.failed");
		}
		this.mc.ingameGUI.getChatGUI().printChatMessage(message.toStringWithFormatting(true));
	}
}
