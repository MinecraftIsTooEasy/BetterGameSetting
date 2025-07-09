package moddedmite.xylose.bettergamesetting.util;

import net.minecraft.*;

public class GuiScreenPanoramaHelp extends GuiMainMenu {
	public static GuiMainMenu panoramaDummy = new GuiMainMenu();
	private static long lastPanoramaTick = System.nanoTime();
	private static float lastTickDelta = 0;

	public static void drawPanorama(GuiScreen screen) {
		if (screen == null) return;
		if (screen.width != panoramaDummy.width || screen.height != panoramaDummy.height) {
			panoramaDummy.setWorldAndResolution(screen.mc, screen.width, screen.height);
		}
		float tickDelta = screen.mc.timer.renderPartialTicks;
		if (System.nanoTime() - lastPanoramaTick > 50000000 || tickDelta < lastTickDelta) {
			panoramaDummy.updateScreen();
			lastPanoramaTick = System.nanoTime();
		}
		lastTickDelta = tickDelta;
		panoramaDummy.renderSkybox(0, 0, tickDelta);
//		screen.drawGradientRect(0, 0, screen.width, screen.height, 0x80FFFFFF, 0x00FFFFFF);
//		screen.drawGradientRect(0, 0, screen.width, screen.height, 0x00000000, 0x80000000);
	}
}
