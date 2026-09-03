package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.audio.SoundHandler;
import moddedmite.xylose.bettergamesetting.client.gui.GuiSubtitleOverlay;

public interface IClient {
	default SoundHandler getSoundHandler() {
		return null;
	}

	default GuiSubtitleOverlay getGuiSubtitleOverlay() {
		return null;
	}
}
