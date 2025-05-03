package moddedmite.xylose.bettergamesetting.mixin.sound.client.audio;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Redirect(method = "playStreaming", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;setVolume(Ljava/lang/String;F)V"))
    public void recordVolume(SoundSystem instance, String sourcename, float value) {
        instance.setVolume(sourcename, ((IGameSetting) Minecraft.getMinecraft().gameSettings).getRecordVolume() * value);
    }
}
