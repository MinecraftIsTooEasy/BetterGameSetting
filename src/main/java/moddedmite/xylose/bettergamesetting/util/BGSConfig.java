package moddedmite.xylose.bettergamesetting.util;

import net.xiaoyu233.fml.config.ConfigEntry;
import net.xiaoyu233.fml.config.ConfigRegistry;
import net.xiaoyu233.fml.config.ConfigRoot;
import net.xiaoyu233.fml.util.FieldReference;
import org.apache.commons.lang3.Range;

import java.io.File;

public class BGSConfig {
    public static final ConfigRegistry INSTANCE;

    public static final FieldReference<Float> LightOptionLimit = new FieldReference<>(Constants.GAMMA_MAX);
    public static final FieldReference<Boolean> useModernCreateWorldGui = new FieldReference<>(true);
    public static final FieldReference<Boolean> useFontFix = new FieldReference<>(true);
    public static final FieldReference<Boolean> freeDevAllowCheat = new FieldReference<>(true);

    public static final ConfigRoot ROOT = ConfigRoot.create(1)
            .addEntry(ConfigEntry.of("Gamma Option Max Limit", LightOptionLimit).withComment("亮度选项上限值"))
            .addEntry(ConfigEntry.of("Use Modern Create World Gui", useModernCreateWorldGui).withComment("使用1.19.4+的创建世界界面"))
            .addEntry(ConfigEntry.of("Use Font Fix", useModernCreateWorldGui).withComment("开启字体修复"))
            .addEntry(ConfigEntry.of("Free Dev Allow Cheat", freeDevAllowCheat).withComment("免dev允许作弊"))
            ;

    public static final File CONFIG_FILE = new File("BetterGameSetting.json");

    static {
        INSTANCE = new ConfigRegistry(BGSConfig.ROOT, BGSConfig.CONFIG_FILE);
        INSTANCE.reloadConfig();
    }
}
