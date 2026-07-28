package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import moddedmite.xylose.bettergamesetting.client.gui.gamerule.GuiGameRules;
import moddedmite.xylose.bettergamesetting.util.GuiScreenPanoramaHelp;
import moddedmite.xylose.bettergamesetting.util.Mth;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static moddedmite.xylose.bettergamesetting.util.Constants.*;

@Mixin(value = Minecraft.class, priority = 9999)
public abstract class MinecraftMixin {
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiScreen currentScreen;
    @Shadow public EntityClientPlayerMP thePlayer;
    @Shadow private boolean fullscreen;
    @Shadow public int displayWidth;
    @Shadow public int displayHeight;
    @Shadow private int tempDisplayWidth;
    @Shadow private int tempDisplayHeight;
    @Shadow public abstract void resize(int par1, int par2);

    @Redirect(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;gammaSetting:F", opcode = Opcodes.PUTFIELD))
    private void keepGammaAndOptionsBounds(GameSettings instance, float value) {
        GameSettings options = this.gameSettings;
        options.limitFramerate = Mth.clamp(options.limitFramerate, FPS_LIMIT_MIN, FPS_LIMIT_MAX, FPS_LIMIT_DEFAULT);
        options.fovSetting = Mth.clamp((int) options.fovSetting, FOV_MIN, FOV_MAX, FOV_DEFAULT);
        options.renderDistance = Mth.clamp(options.renderDistance, RENDER_DISTANCE_MIN, RENDER_DISTANCE_MAX, RENDER_DISTANCE_DEFAULT);
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
        if (!(this.gameSettings.limitFramerate >= FPS_LIMIT_MAX)) {
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
        GuiGameRules.applyPendingRules(ReflectHelper.dyCast(this));
    }

    /**
     * @author Arminias & Xy_Luce
     * @reason Optimized the performance of full-screen and change to windowed full-screen
     */
    @Overwrite
    public void toggleFullscreen() {
        if (Main.is_MITE_DS) {
            this.fullscreen = false;
            return;
        }
        try {
            this.fullscreen = !this.fullscreen;
            if (this.fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setResizable(false);
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                Display.setResizable(true);
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;
                
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            }
            
            if (this.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            }
            
            Display.setVSyncEnabled(this.gameSettings.isVsyncEnabled());
            Display.update();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}
