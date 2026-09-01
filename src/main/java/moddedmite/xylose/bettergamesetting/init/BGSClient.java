package moddedmite.xylose.bettergamesetting.init;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.GameRules;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.config.ConfigRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BGSClient implements ClientModInitializer, ModInitializer {
    public static String resourceId = "bgs";
    public static final Logger logger = LogManager.getLogger("BGS");
    public static final Map<String, String> DEFAULT_GAMERULE_VALUE = new HashMap<>();
    public static final GameRules gameRules = new GameRules();
    public static Map<String, String> pendingRules = new HashMap<>();
    public static int scrollAmount = 0;

    @Override
    public void onInitializeClient() {
        ModResourceManager.addResourcePackDomain(resourceId);
        Handlers.Keybinding.register(CustomKeys::registerKeybindings);
    }

    @Override
    public void onInitialize() {
    }

    @Override
    public Optional<ConfigRegistry> createConfig() {
        return Optional.of(BGSConfig.INSTANCE);
    }
}
