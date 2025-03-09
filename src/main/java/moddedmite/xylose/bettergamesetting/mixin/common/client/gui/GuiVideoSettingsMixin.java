package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.client.gui.button.GuiScaleSlider;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiVideoSettings.class)
public class GuiVideoSettingsMixin extends GuiScreen {
//    @Shadow private static EnumOptions[] videoOptions;
//
//    @Shadow private GameSettings guiGameSettings;
//
//    @Inject(method = "initGui", at = @At(value = "TAIL"))
//    private void enabledAll(CallbackInfo ci) {
//        for (Object o : this.buttonList) {
//            ((GuiButton) o).enabled = true;
//        }
//        int j = 1000;
//        int max = 1;
//
//        while (max < j && mc.displayWidth / (max + 1) >= 320 && mc.displayHeight / (max + 1) >= 240) {
//            ++max;
//        }
//
//        if (max != 1)
//            max--;
//
//        for (EnumOptions var7 : videoOptions) {
//            if (var7 == EnumOptions.GUI_SCALE) {
//                this.buttonList.add(new GuiScaleSlider(var7.returnEnumOrdinal(), this.width / 2 - 155, this.height / 7 + 72, guiGameSettings, 0, max));
//            }
//        }
//    }
}
