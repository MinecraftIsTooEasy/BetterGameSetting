package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.MusicTicker;

public interface IWorldProvider {
	default MusicTicker.MusicType getMusicType() {
		return null;
	}
}
