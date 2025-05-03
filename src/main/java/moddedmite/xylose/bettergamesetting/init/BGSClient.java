package moddedmite.xylose.bettergamesetting.init;

import net.fabricmc.api.ClientModInitializer;
import net.xiaoyu233.fml.ModResourceManager;

public class BGSClient implements ClientModInitializer {
    public static String resourceId = "bgs";
    @Override
    public void onInitializeClient() {
        ModResourceManager.addResourcePackDomain(resourceId);
    }
}
