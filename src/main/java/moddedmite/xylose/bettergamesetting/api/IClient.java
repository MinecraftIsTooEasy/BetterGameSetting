package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.SoundHandler;

public interface IClient {
	default SoundHandler getSoundHandler() {
		return null;
	}
}
