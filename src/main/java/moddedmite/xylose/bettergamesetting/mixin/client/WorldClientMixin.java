package moddedmite.xylose.bettergamesetting.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.client.audio.PositionedSoundRecord;
import moddedmite.xylose.bettergamesetting.client.audio.SoundCategory;
import moddedmite.xylose.bettergamesetting.client.audio.SoundEvent;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.SoundManager;
import net.minecraft.WorldClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldClient.class)
public class WorldClientMixin {
	@Shadow @Final private Minecraft mc;
	
	@WrapOperation(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;func_92070_a(Ljava/lang/String;FFFFFI)V"))
	private void replaceSoundManager(SoundManager instance, String sound, float x, float y, float z, float volume, float pitch, int distanceDelay, Operation<Void> original) {
		this.mc.getSoundHandler().playDelayedSound(new PositionedSoundRecord(new SoundEvent(new ResourceLocation(sound), SoundCategory.MASTER), SoundCategory.MASTER, volume, pitch, x, y, z), distanceDelay);
	}
	
	@WrapOperation(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSound(Ljava/lang/String;FFFFF)V"))
	private void replaceSoundManager(SoundManager instance, String sound, float x, float y, float z, float volume, float pitch, Operation<Void> original) {
		this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new SoundEvent(new ResourceLocation(sound), SoundCategory.MASTER), SoundCategory.MASTER, volume, pitch, x, y, z));
	}
}
