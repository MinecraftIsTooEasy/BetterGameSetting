package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.GuiScreenPanoramaHelp;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = Minecraft.class, priority = 9999)
public abstract class MinecraftMixin {
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiScreen currentScreen;
    @Shadow public EntityClientPlayerMP thePlayer;

    @Shadow public abstract IntegratedServer getIntegratedServer();

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
        ScreenUtil.instance = new ScreenUtil();
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
        for (int key = 0; key < 9; ++key) {
            if (Keyboard.getEventKey() == CustomKeys.inventoryKeyProvider(key)) {
                this.thePlayer.inventory.currentItem = key;
                break;
            }
        }
    }

    @Inject(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;displayGuiScreen(Lnet/minecraft/GuiScreen;)V"))
    private void launchIntegratedServer(CallbackInfo ci) {
        WorldServer overworld = this.getIntegratedServer().worldServers[0];
        if (overworld != null && !BGSClient.pendingRules.isEmpty()) {
            for (Map.Entry<String, String> entry : BGSClient.pendingRules.entrySet()) {
                overworld.getGameRules().setOrCreateGameRule(entry.getKey(), entry.getValue());
                FishModLoader.LOGGER.info("Applied game rule: {} = {}", entry.getKey(), entry.getValue());
            }
            BGSClient.pendingRules.clear();
        }
    }
}
