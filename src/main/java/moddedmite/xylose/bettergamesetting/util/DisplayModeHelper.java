package moddedmite.xylose.bettergamesetting.util;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayModeHelper {

    public static String displayModeInfo;

    private static final Pattern DISPLAY_MODE_PATTERN = Pattern.compile(
            "^\\s*(\\d+)\\s*x\\s*(\\d+)\\s*x\\s*(\\d+)\\s*@\\s*(\\d+)Hz\\s*$"
    );
    
    public static List<DisplayMode> resolutions = new ArrayList<>();
    private static boolean resolutionsInitialized = false;
    
    public static void initDisplay() throws LWJGLException {
        if (!resolutionsInitialized) {
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            for (DisplayMode mode : modes) {
                displayModeInfo = mode.toString();
                if (!containsResolution(mode)) {
                    resolutions.add(mode);
                }
            }
//        displayModeInfo = Display.getDisplayMode().toString();
            resolutions.sort((a, b) -> {
                if (a.getWidth() != b.getWidth()) return a.getWidth() - b.getWidth();
                return a.getHeight() - b.getHeight();
            });
            resolutionsInitialized = true;
        }
    }
    
    private static boolean containsResolution(DisplayMode mode) {
        return resolutions.stream().anyMatch(m ->
                m.getWidth() == mode.getWidth() &&
                        m.getHeight() == mode.getHeight() &&
                        m.getFrequency() == mode.getFrequency() &&
                        m.getBitsPerPixel() == mode.getBitsPerPixel()
        );
    }
    
    
    public static int[] parseDisplayModeString(String str) {
        Matcher matcher = DISPLAY_MODE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid resolution format: " + str);
        }

        int width = Integer.parseInt(matcher.group(1));
        int height = Integer.parseInt(matcher.group(2));
        int refreshRate = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;
        int bitsPerPixel = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;

        return new int[] { width, height, refreshRate, bitsPerPixel };
    }

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
        return null;
    }

    public static DisplayMode getDisplayModeFromString(String str) {
        int[] params = parseDisplayModeString(str);
        DisplayMode mode = Display.getDisplayMode();
        if (mode == null) {
            throw new IllegalArgumentException("Unsupported resolutions: " + str);
        }
        return mode;
    }
}
