package moddedmite.xylose.bettergamesetting.init;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.GameRules;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.config.ConfigRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
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
    
    public static URL getURLForSoundResource(final ResourceLocation id) {
        String s = String.format("%s:%s:%s", "mcsounddomain", id.getResourceDomain(), id.getResourcePath());
        URLStreamHandler urlstreamhandler = new URLStreamHandler() {
            protected URLConnection openConnection(URL p_openConnection_1_) {
                return new URLConnection(p_openConnection_1_) {
                    public void connect() throws IOException {
                    }
                    
                    public InputStream getInputStream() throws IOException {
                        return Minecraft.getMinecraft().getResourceManager().getResource(id).getInputStream();
                    }
                };
            }
        };
        
        try {
            return new URL(null, s, urlstreamhandler);
        } catch (MalformedURLException var4) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }
}
