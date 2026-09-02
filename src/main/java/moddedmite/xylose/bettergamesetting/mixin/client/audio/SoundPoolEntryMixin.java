package moddedmite.xylose.bettergamesetting.mixin.client.audio;

import net.minecraft.ResourceLocation;
import net.minecraft.SoundPoolEntry;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.net.URL;

//TODO remove
@Mixin(SoundPoolEntry.class)
public class SoundPoolEntryMixin {
//	@Shadow @Final private String soundName;
//	@Shadow @Final private URL soundUrl;
//
//	@Unique private boolean streaming;
//	@Unique private double pitch;
//	@Unique private double volume;
//
//	public ResourceLocation getSoundPoolEntryLocation() {
//		return new ResourceLocation(this.soundName + this.soundUrl.getPath());
//	}
//
//	public SoundPoolEntry cloneEntry() {
//		return ReflectHelper.dyCast(this);
//	}
//
//	public double getPitch() {
//		return this.pitch;
//	}
//
//	public void setPitch(double pitch) {
//		this.pitch = pitch;
//	}
//
//	public double getVolume() {
//		return this.volume;
//	}
//
//	public void setVolume(double volume) {
//		this.volume = volume;
//	}
//
//	public boolean isStreaming() {
//		return this.streaming;
//	}
//
//	public void setStreaming(boolean streaming) {
//		this.streaming = streaming;
//	}
}
