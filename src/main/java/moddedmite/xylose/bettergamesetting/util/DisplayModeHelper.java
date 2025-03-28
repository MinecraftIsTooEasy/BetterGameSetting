package moddedmite.xylose.bettergamesetting.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayModeHelper {

    public static String displayModeInfo;

    // 正则表达式匹配格式：宽度x高度[@刷新率Hz][-色深bit]
    private static final Pattern DISPLAY_MODE_PATTERN = Pattern.compile(
            "^\\s*(\\d+)\\s*x\\s*(\\d+)\\s*x\\s*(\\d+)\\s*@\\s*(\\d+)Hz\\s*$"
    );

    public static void initDisplay() throws LWJGLException {
        DisplayMode[] modes = Display.getAvailableDisplayModes();
//        for (DisplayMode mode : modes) {
//            displayModeInfo = mode.getWidth() + "x" + mode.getHeight() + " @ " + mode.getFrequency() + "Hz";
//        }
        displayModeInfo = Display.getDisplayMode().toString();
    }

    /**
     * 将字符串解析为 DisplayMode 参数
     * @param str 格式如 "1920x1080@60Hz-32bit"
     * @return 包含 width, height, refreshRate, bitsPerPixel 的数组
     * @throws IllegalArgumentException 如果格式无效
     */
    public static int[] parseDisplayModeString(String str) {
        Matcher matcher = DISPLAY_MODE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("无效的分辨率格式: " + str);
        }

        int width = Integer.parseInt(matcher.group(1));
        int height = Integer.parseInt(matcher.group(2));
        int refreshRate = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0; // 0 表示未指定
        int bitsPerPixel = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0; // 0 表示未指定

        return new int[] { width, height, refreshRate, bitsPerPixel };
    }

    /**
     * 从可用模式中查找匹配的 DisplayMode
     * @param params 包含 width, height, refreshRate, bitsPerPixel 的数组
     * @return 匹配的 DisplayMode，找不到时返回 null
     */
    public static DisplayMode findMatchingDisplayMode(int[] params) throws LWJGLException {
        for (DisplayMode mode : Display.getAvailableDisplayModes()) {
            boolean widthMatch = (mode.getWidth() == params[0]);
            boolean heightMatch = (mode.getHeight() == params[1]);
            boolean refreshMatch = (params[2] == 0 || mode.getFrequency() == params[2]);
            boolean bitsMatch = (params[3] == 0 || mode.getBitsPerPixel() == params[3]);

            if (widthMatch && heightMatch && refreshMatch && bitsMatch) {
                return mode;
            }
        }
        return null; // 未找到匹配项
    }

    /**
     * 从字符串获取 DisplayMode
     * @param str 分辨率字符串
     * @return 匹配的 DisplayMode
     * @throws IllegalArgumentException 如果解析失败或找不到匹配项
     */
    public static DisplayMode getDisplayModeFromString(String str) throws LWJGLException {
        int[] params = parseDisplayModeString(str);
        DisplayMode mode = Display.getDisplayMode();
        if (mode == null) {
            throw new IllegalArgumentException("不支持的分辨率: " + str);
        }
        return mode;
    }
}
