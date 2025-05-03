package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import net.minecraft.GuiChat;
import net.minecraft.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class GuiChatMixin extends GuiScreen {

    @Inject(method = "updateScreen", at = @At("TAIL"))
    public void setChatSrceenFocus(CallbackInfo ci) {
        if (mc.inGameHasFocus) {
            mc.setIngameNotInFocus();
        }
    }

//    @Inject(method = "onGuiClosed", at = @At("TAIL"))
//    public void disableFocus(CallbackInfo ci) {
//        if (!mc.inGameHasFocus && this.mc.currentScreen != null && !this.mc.currentScreen.allowsImposedChat()) {
//            mc.setIngameFocus();
//        }
//    }
}
