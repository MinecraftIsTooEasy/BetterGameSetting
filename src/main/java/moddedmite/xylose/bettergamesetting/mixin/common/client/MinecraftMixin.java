package moddedmite.xylose.bettergamesetting.mixin.common.client;

import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import net.minecraft.*;
import net.minecraft.Debug;
import net.minecraft.client.main.Main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 9999)
public class MinecraftMixin {
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public GuiScreen currentScreen;

    @Redirect(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;gammaSetting:F", opcode = Opcodes.PUTFIELD))
    private void inject(GameSettings instance, float value) {
        if (this.gameSettings.limitFramerate < 10)
            this.gameSettings.limitFramerate = 120;
        if (this.gameSettings.fovSetting < 30)
            this.gameSettings.fovSetting = 70;
        if (this.gameSettings.getRenderDistance() < 2)
            this.gameSettings.renderDistance = 12;
    }

    /**
     * @author Xy_Lose
     * @reason break Fps limit & optimize GuiMainMenu
     */
    @Overwrite
    private int getLimitFramerate() {
        if (this.currentScreen != null && (this.currentScreen instanceof GuiMainMenu)) {
            return 24;
        }
        if (!(this.gameSettings.limitFramerate >= 260)) {
            return this.gameSettings.limitFramerate;
        }
        return 9999;
    }

}
