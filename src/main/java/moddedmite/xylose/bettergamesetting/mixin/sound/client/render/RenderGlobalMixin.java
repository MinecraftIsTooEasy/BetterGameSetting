package moddedmite.xylose.bettergamesetting.mixin.sound.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Minecraft;
import net.minecraft.RenderGlobal;
import net.minecraft.SoundManager;
import net.minecraft.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Shadow private Minecraft mc;

    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 2))
    public void bowVolume(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getPlayerVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 3))
    public void doorVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 4))
    public void doorVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 5))
    public void fizzVolume(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getPlayerVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 6))
    public void ghastVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getHostileVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 7))
    public void ghastVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getHostileVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 8))
    public void ghastVolume_2(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getHostileVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 9))
    public void zombieDoorVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 10))
    public void zombieDoortVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 11))
    public void zombieDoorVolume_2(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 12))
    public void batVolume(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getNeutralVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 13))
    public void zombieInfectVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getHostileVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 14))
    public void zombieInfectVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getHostileVolume() * par7Str, par8, par9);
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
