package moddedmite.xylose.bettergamesetting.mixin.sound.network;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Entity;
import net.minecraft.Minecraft;
import net.minecraft.NetClientHandler;
import net.minecraft.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {
    @Shadow private Minecraft mc;

    @WrapOperation(method = "handleCollect", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSoundAtEntity(Lnet/minecraft/Entity;Ljava/lang/String;FF)V"))
    public void collectVolume(WorldClient instance, Entity entity, String s, float v, float w, Operation<Void> original) {
        instance.playSoundAtEntity(entity, s, ((IGameSetting) this.mc.gameSettings).getPlayerVolume() * v, w);
    }

    @WrapOperation(method = "handleEntityFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 4))
    public void breakVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getPlayerVolume() * par7Str, par8, par9);
    }

    @WrapOperation(method = "handleEntityFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 5))
    public void breakVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getPlayerVolume() * par7Str, par8, par9);
    }

    @WrapOperation(method = "handleEntityFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 6))
    public void waterVolume(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getAmbientVolume() * par7Str, par8, par9);
    }
}
