package moddedmite.xylose.bettergamesetting.init;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.xiaoyu233.fml.util.EnumExtends;

public class BGSEarlyRiser implements PreLaunchEntrypoint {
    public void onPreLaunch() {
        EnumExtends.OPTIONS.addEnum("RECORDS", "options.record", true, false);
        EnumExtends.OPTIONS.addEnum("WEATHER", "options.weather", true, false);
        EnumExtends.OPTIONS.addEnum("BLOCKS", "options.block", true, false);
        EnumExtends.OPTIONS.addEnum("MOBS", "options.hostile", true, false);
        EnumExtends.OPTIONS.addEnum("ANIMALS", "options.neutral", true, false);
        EnumExtends.OPTIONS.addEnum("PLAYERS", "options.player", true, false);
        EnumExtends.OPTIONS.addEnum("AMBIENT", "options.ambient", true, false);
        EnumExtends.OPTIONS.addEnum("UI", "options.ui", true, false);
//        EnumExtends.OPTIONS.addEnum("MIPMAP_LEVELS", "options.mipmapLevels", true, false);
//        EnumExtends.OPTIONS.addEnum("ANISOTROPIC_FILTERING", "options.anisotropicFiltering", true, false);
        EnumExtends.OPTIONS.addEnum("FORCE_UNICODE_FONT", "options.forceUnicodeFont", false, true);
//        EnumExtends.OPTIONS.addEnum("FULLSCREEN_RESOLUTION", "options.fullscreenResolution", true, false);
        EnumExtends.OPTIONS.addEnum("TRANSPARENT_BACKGROUND", "options.transparentBackground", false, true);
        EnumExtends.OPTIONS.addEnum("HIGHLIGHT_BUTTON_TEXT", "options.highlightButtonText", false, true);
    }
}
