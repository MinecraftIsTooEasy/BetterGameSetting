package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Calendar;

@Mixin(value = GuiMainMenu.class, priority = 9999)
public abstract class GuiMainMenuMixin extends GuiScreen {
    @Shadow private GuiButton minecraftRealmsButton;
    @Shadow private ResourceLocation field_110351_G;

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disableMITEResourcePack(CallbackInfo ci, Calendar var1, boolean var2, int var3) {
        if (Minecraft.MITE_resource_pack == null) {
            this.buttonList.add(new GuiButtonForum(6, this.width / 2 + 124 - 20, var3 + 72 + 12));
        }
    }

    @Inject(method = "func_130022_h", at = @At("TAIL"))
    private void func_130022_h(CallbackInfo ci) {
        this.minecraftRealmsButton.drawButton = !Main.is_MITE_DS;
    }

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void disableMITEResourcePack(int par1, int par2, CallbackInfo ci, GuiButton button_singleplayer, GuiButton button_multiplayer) {
        if (Minecraft.MITE_resource_pack == null) {
            button_singleplayer.enabled = true;
            button_multiplayer.enabled = true;
        }
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawRect(IIIII)V", ordinal = 1))
    private boolean disableMITEResourcePack(int i, int j, int k, int l, int m) {
        return ((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 4))
    private boolean disableMITEResourcePack(GuiMainMenu instance, FontRenderer fontRenderer, String string, int i, int j, int k) {
        return ((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 5))
    private boolean disableMITEResourcePack_1(GuiMainMenu instance, FontRenderer fontRenderer, String string, int i, int j, int k) {
        return ((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

    @WrapWithCondition(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;displayGuiScreen(Lnet/minecraft/GuiScreen;)V", ordinal = 1))
    private boolean disableMITEResourcePack_1(Minecraft instance, GuiScreen var3) {
        return ((IGameSetting) this.mc.gameSettings).getResourcePacks().isEmpty();
    }

//    @ModifyConstant(method = "initGui", constant = @Constant(intValue = 256, ordinal = 0))
//    private int wider(int constant) {
//        return this.mc.displayWidth;
//    }
//    @ModifyConstant(method = "initGui", constant = @Constant(intValue = 256, ordinal = 1))
//    private int higher(int constant) {
//        return this.mc.displayHeight;
//    }
//
//    /**
//     * @author
//     * @reason
//     */
//    @Overwrite
//    private void rotateAndBlurSkybox(float par1) {
//        this.mc.getTextureManager().bindTexture(this.field_110351_G);
//        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, mc.displayWidth, mc.displayHeight);
//        GL11.glEnable(3042);
//        GL11.glBlendFunc(770, 771);
//        GL11.glColorMask(true, true, true, true);
//        Tessellator var2 = Tessellator.instance;
//        var2.startDrawingQuads();
//        var2.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
//        int var5 = this.width;
//        int var6 = this.height;
//        var2.addVertexWithUV(var5, var6, this.zLevel, 0.0f, 0.0);
//        var2.addVertexWithUV(var5, 0.0, this.zLevel, 1.0f, 0.0);
//        var2.addVertexWithUV(0.0, 0.0, this.zLevel, 1.0f, 1.0);
//        var2.addVertexWithUV(0.0, var6, this.zLevel, 0.0f, 1.0);
//        var2.draw();
//    }
//
//    @WrapOperation(method = "renderSkybox", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glViewport(IIII)V"))
//    private void renderSkybox(int x, int y, int width, int height, Operation<Void> original) {
//        GL11.glViewport(x, y, mc.displayWidth, mc.displayHeight);
//    }
//
//    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawGradientRect(IIIIII)V"))
//    private void disableBlur(GuiMainMenu instance, int i, int j, int k, int l, int m, int n) {
//    }
}