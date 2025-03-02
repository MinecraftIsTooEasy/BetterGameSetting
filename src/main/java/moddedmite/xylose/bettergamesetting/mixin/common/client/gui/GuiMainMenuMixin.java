package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Calendar;
import java.util.Objects;

@Mixin(value = GuiMainMenu.class, priority = 9999)
public class GuiMainMenuMixin extends GuiScreen {
    @Shadow
    private GuiButton minecraftRealmsButton;

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;isDemo()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disMITEResourcePack(CallbackInfo ci, Calendar var1, boolean var2, int var3) {
        if (Minecraft.MITE_resource_pack == null) {
            this.buttonList.add(new GuiButtonForum(6, this.width / 2 + 124 - 20, var3 + 72 + 12));
        }
    }

    @Inject(method = "func_130022_h", at = @At("TAIL"))
    private void func_130022_h(CallbackInfo ci) {
        this.minecraftRealmsButton.drawButton = !Main.is_MITE_DS;
    }

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disMITEResourcePack(int par1, int par2, CallbackInfo ci, GuiButton button_singleplayer, GuiButton button_multiplayer) {
        if (Minecraft.MITE_resource_pack == null) {
            button_singleplayer.enabled = true;
            button_multiplayer.enabled = true;
        }
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawRect(IIIII)V", ordinal = 1))
    private boolean disMITEResourcePack(int i, int j, int k, int l, int m) {
//        return Objects.equals(this.mc.getResourcePackRepository().getResourcePackName(), this.mc.mcDefaultResourcePack.getPackName());
        return !((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 4))
    private boolean disMITEResourcePack(GuiMainMenu instance, FontRenderer fontRenderer, String string, int i, int j, int k) {
//        return Objects.equals(this.mc.getResourcePackRepository().getResourcePackName(), this.mc.mcDefaultResourcePack.getPackName());
        return !((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 5))
    private boolean disMITEResourcePack_1(GuiMainMenu instance, FontRenderer fontRenderer, String string, int i, int j, int k) {
//        return Objects.equals(this.mc.getResourcePackRepository().getResourcePackName(), this.mc.mcDefaultResourcePack.getPackName());
        return !((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }
}
