package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.api.IWorldProvider;
import moddedmite.xylose.bettergamesetting.client.audio.MusicTicker;
import net.minecraft.WorldProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldProvider.class)
public class WorldProviderMixin implements IWorldProvider {
	@Override
	public MusicTicker.MusicType getMusicType() {
		return null;
	}
}
