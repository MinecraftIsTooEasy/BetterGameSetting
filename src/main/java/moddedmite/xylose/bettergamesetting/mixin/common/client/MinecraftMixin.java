package moddedmite.xylose.bettergamesetting.mixin.common.client;

import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import moddedmite.xylose.bettergamesetting.util.GuiScreenPanoramaHelp;
import net.minecraft.*;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 9999)
public class MinecraftMixin {
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiScreen currentScreen;
    @Shadow public EntityClientPlayerMP thePlayer;

    @Redirect(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;gammaSetting:F", opcode = Opcodes.PUTFIELD))
    private void keepGammaAndOptionsBounds(GameSettings instance, float value) {
        if (this.gameSettings.limitFramerate < 10 || this.gameSettings.limitFramerate > 260)
            this.gameSettings.limitFramerate = 120;
        if (this.gameSettings.fovSetting < 30 || this.gameSettings.fovSetting > 110)
            this.gameSettings.fovSetting = 70;
        if (this.gameSettings.renderDistance < 2 || this.gameSettings.renderDistance > 24)
            this.gameSettings.renderDistance = 12;
    }

    /**
     * @author Xy_Lose
     * @reason break Fps limit & optimize GuiMainMenu
     */
    @Overwrite
    private int getLimitFramerate() {
        if (this.currentScreen != null && (this.currentScreen instanceof GuiMainMenu)) {
            return 60;
        }
        if (!(this.gameSettings.limitFramerate >= 260)) {
            return this.gameSettings.limitFramerate;
        }
        return 9999;
    }

    @Redirect(method = "startGame", at = @At(value = "NEW", target = "net/minecraft/GuiMainMenu"))
    private GuiMainMenu unificationPanorama() {
        return GuiScreenPanoramaHelp.panoramaDummy;
    }

    @ModifyArg(method = "screenshotListener", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"))
    private int modifyPrintScreenKey(int key) {
        return CustomKeys.printScreenKeyProvider();
    }

    @ModifyArg(method = "screenshotListenerForForcedRendering", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"))
    private int modifyPrintScreenKey_1(int key) {
        return CustomKeys.printScreenKeyProvider();
    }

    @ModifyConstant(method = "runTick", constant = @Constant(intValue = 63))
    private int modifyPersonViewKey(int key) {
        return CustomKeys.personViewKeyProvider();
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryPlayer;currentItem:I", opcode = Opcodes.PUTFIELD))
    private void disableVanillaItemSwitch(InventoryPlayer instance, int value) {}

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;showDebugInfo:Z", opcode = Opcodes.GETFIELD, ordinal = 2))
    private void customItemSwitch(CallbackInfo ci) {
        int var1;
        for (var1 = 0; var1 < 9; ++var1) {
            if (Keyboard.getEventKey() == CustomKeys.inventoryKeyProvider(var1)) {
                this.thePlayer.inventory.currentItem = var1;
                break;
            }
        }
    }
}
