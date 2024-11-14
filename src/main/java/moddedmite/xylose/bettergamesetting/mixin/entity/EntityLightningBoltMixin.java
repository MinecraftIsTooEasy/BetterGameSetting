package moddedmite.xylose.bettergamesetting.mixin.entity;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.EntityLightningBolt;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityLightningBolt.class)
public class EntityLightningBoltMixin {

    @ModifyConstant(method = "onUpdate", constant = @Constant(floatValue = 10000.0f))
    private float modifyLightningBoltSound(float constant) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getWeatherVolume() * constant;
    }
    @ModifyConstant(method = "onUpdate", constant = @Constant(floatValue = 2.0f))
    private float modifyLightningBoltSound1(float constant) {
        return ((IGameSetting) Minecraft.getMinecraft().gameSettings).getWeatherVolume() * constant;
    }
}
