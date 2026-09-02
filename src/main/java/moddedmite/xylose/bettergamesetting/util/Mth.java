package moddedmite.xylose.bettergamesetting.util;

import java.util.Random;
import java.util.UUID;

/**
 * MathHelper Extra
 */
public class Mth {
    public static float normalizeValue(float value, float min, float max, float step) {
        float v = snapToStepClamp(value, min, max, step);
        return clamp((v - min) / (max - min), 0.0f, 1.0f);
    }

    public static float denormalizeValue(float value, float min, float max, float step) {
        float v = min + ((max - min) * clamp(value, 0.0f, 1.0f));
        return snapToStepClamp(v, min, max, step);
    }

    public static float snapToStepClamp(float value, float min, float max, float step) {
        return clamp(snapToStep(value, step), min, max);
    }

    public static float snapToStep(float value, float step) {
        if (step > 0.0f) {
            return step * Math.round(value / step);
        }
        return value;
    }

    public static int clamp(int i, int j, int k) {
        return Math.min(Math.max(i, j), k);
    }

    public static long clamp(long l, long m, long n) {
        return Math.min(Math.max(l, m), n);
    }

    public static float clamp(float f, float g, float h) {
        return f < g ? g : Math.min(f, h);
    }

    public static double clamp(double d, double e, double f) {
        return d < e ? e : Math.min(d, f);
    }
    
    public static int clamp(int value, int min, int max, int defaultValue) {
        return value < min || value > max ? defaultValue : value;
    }

    public static float lerp(float f, float g, float h) {
        return g + f * (h - g);
    }

    public static double lerp(double d, double e, double f) {
        return e + d * (f - e);
    }

    public static double lerp2(double d, double e, double f, double g, double h, double i) {
        return lerp(e, lerp(d, f, g), lerp(d, h, i));
    }

    public static double lerp3(double d, double e, double f, double g, double h, double i, double j, double k, double l, double m, double n) {
        return lerp(f, lerp2(d, e, g, h, i, j), lerp2(d, e, k, l, m, n));
    }

    public static UUID getRandomUUID(Random rand) {
        long i = rand.nextLong() & -61441L | 16384L;
        long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        return new UUID(i, j);
    }
}
