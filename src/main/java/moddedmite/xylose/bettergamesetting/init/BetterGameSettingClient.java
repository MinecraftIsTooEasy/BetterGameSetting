package moddedmite.xylose.bettergamesetting.init;

import moddedmite.xylose.bettergamesetting.util.DisplayModeHelper;
import net.fabricmc.api.ClientModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import org.lwjgl.LWJGLException;

public class BetterGameSettingClient implements ClientModInitializer {
    public static String resourceId = "bgs";
    @Override
    public void onInitializeClient() {
        ModResourceManager.addResourcePackDomain(resourceId);
    }
}
