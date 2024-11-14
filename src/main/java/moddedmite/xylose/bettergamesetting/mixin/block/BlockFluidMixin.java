package moddedmite.xylose.bettergamesetting.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.BlockFluid;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFluid.class)
public class BlockFluidMixin {
    @WrapOperation(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;playSound(DDDLjava/lang/String;FFZ)V"))
    public final void randomDisplayTick(World instance, double par1, double par3, double par5, String par7Str, float par8, float par9, boolean par10, Operation<Void> original) {
        instance.playSound(par1, par3, par5, par7Str, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getAmbientVolume() * par8, par9, par10);
    }
}
