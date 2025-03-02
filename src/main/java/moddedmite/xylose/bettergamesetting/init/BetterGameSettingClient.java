package moddedmite.xylose.bettergamesetting.init;

import net.fabricmc.api.ClientModInitializer;
import net.xiaoyu233.fml.ModResourceManager;

public class BetterGameSettingClient implements ClientModInitializer {
    public static String modid = "bgs";
    @Override
    public void onInitializeClient() {
        ModResourceManager.addResourcePackDomain(modid);
    }
}
