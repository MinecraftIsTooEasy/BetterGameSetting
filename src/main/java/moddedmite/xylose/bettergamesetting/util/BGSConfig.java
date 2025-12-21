package moddedmite.xylose.bettergamesetting.util;

import net.xiaoyu233.fml.config.ConfigEntry;
import net.xiaoyu233.fml.config.ConfigRegistry;
import net.xiaoyu233.fml.config.ConfigRoot;
import net.xiaoyu233.fml.util.FieldReference;

import java.io.File;

public class BGSConfig {
    public static final ConfigRegistry INSTANCE;

    public static final FieldReference<Float> LightOptionLimit = new FieldReference<>(1.0F);
    public static final FieldReference<Boolean> useModernCreateWorldGui = new FieldReference<>(true);

    public static final ConfigRoot ROOT = ConfigRoot.create(1)
            .addEntry(ConfigEntry.of("Gamma Option Max Limit", LightOptionLimit).withComment("亮度选项上限值"))
            .addEntry(ConfigEntry.of("Use Modern Create World Gui", useModernCreateWorldGui).withComment("使用1.19.4+的创建世界界面"));

    public static final File CONFIG_FILE = new File("BetterGameSetting.json");

    static {
        INSTANCE = new ConfigRegistry(BGSConfig.ROOT, BGSConfig.CONFIG_FILE);
        INSTANCE.reloadConfig();
    }
}
