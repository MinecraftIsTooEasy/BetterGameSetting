package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Shadow private int renderChunksWide;
    @Shadow private int renderDistance;
    @Shadow private int renderChunksTall;
    @Shadow private int renderChunksDeep;
    @Shadow private WorldClient theWorld;
    @Shadow private Minecraft mc;

    @Inject(method = "loadRenderers", at = @At(value = "FIELD", target = "Lnet/minecraft/RenderGlobal;renderChunksDeep:I", shift = At.Shift.AFTER))
    private void test(CallbackInfo ci) {
        int var1 = Math.min(65, (this.renderDistance * 2) + 1);
        this.renderChunksWide = var1;
        this.renderChunksTall = 16;
        this.renderChunksDeep = var1;
    }

    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSound(Ljava/lang/String;FFFFF)V"))
    private void blockDestroyVolume(SoundManager instance, String var9, float var7, float v, float par1Str, float par2, float par3, Operation<Void> original) {
        instance.playSound(var9, var7, v, par1Str, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par2, par3);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 16))
    public void anvilVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 17))
    public void anvilVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 18))
    public void anvilVolume_2(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
}
