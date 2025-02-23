package moddedmite.xylose.bettergamesetting.mixin.common.client;

import net.minecraft.GameSettings;
import net.minecraft.GuiMainMenu;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Minecraft.class, priority = 9999)
public class MinecraftMixin {
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiScreen currentScreen;

    @Redirect(method = "runGameLoop", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;gammaSetting:F", opcode = Opcodes.PUTFIELD))
    private void inject(GameSettings instance, float value) {
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
