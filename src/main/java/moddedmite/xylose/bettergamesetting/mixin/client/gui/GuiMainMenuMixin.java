package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Calendar;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiScreen {
    @Shadow
    private GuiButton minecraftRealmsButton;

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;isDemo()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disMITEResourcePack(CallbackInfo ci, Calendar var1, boolean var2, int var3) {
        if (Minecraft.MITE_resource_pack == null) {
            this.buttonList.add(new GuiButtonForum(6, this.width / 2 + 124 - 20, var3 + 72 + 12));
        }
    }

    @Overwrite
    private void func_130022_h() {
        this.minecraftRealmsButton.drawButton = !Main.is_MITE_DS;
    }

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disMITEResourcePack(int par1, int par2, CallbackInfo ci, GuiButton button_singleplayer, GuiButton button_multiplayer) {
        if (Minecraft.MITE_resource_pack == null) {
            button_singleplayer.enabled = true;
            button_multiplayer.enabled = true;
        }
    }

    @ModifyConstant(method = "drawScreen", constant = @Constant(stringValue = "MITE Resource Pack 1.6.4 needs to be installed!"))
    private String disMITEResourcePack(String constant) {
        return "";
    }
}
