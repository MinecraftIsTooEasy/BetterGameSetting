package moddedmite.xylose.bettergamesetting.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.minecraft.MathHelper;
import net.minecraft.MetadataSection;
import net.minecraft.MetadataSerializer;
import net.minecraft.Minecraft;
import net.minecraft.Resource;
import net.minecraft.ResourceLocation;
import net.minecraft.ResourcePack;
import net.minecraft.ScaledResolution;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FontPack implements ResourcePack {
    public static volatile int SS = 1;
    private static final int MAX_SS = 4;
    private static final int SUPER = 2;
    private static final byte[] SMOOTH = "{\"texture\":{\"blur\":true,\"clamp\":true}}".getBytes(StandardCharsets.UTF_8);
    private static final String ASCII_PATH = "textures/font/ascii.png";
    private static final String GLYPH_PATH = "font/glyph_sizes.bin";
    private static final String PAGE_PREFIX = "textures/font/unicode_page_";
    private static final String META_SUFFIX = ".mcmeta";
    private static final String PROVIDER_PATH = "font/default.json";
    private static final String ASCII = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";

    private final int res;
    private final int ss;
    private final int cell;
    private final int pad;
    private Font font;
    private int baseline;
    private boolean fontTried;
    private final int rasterSize;
    private final BufferedImage raster;
    private final Graphics2D rasterGraphics;
    private final int[] rasterPixels;
    private final int[] cellPixels;
    private Font rasterFont;
    private final Map<Integer, byte[]> pages = new HashMap<Integer, byte[]>();
    private final Set<Integer> emptyPages = new HashSet<Integer>();
    private byte[] asciiPng;
    private boolean asciiTried;
    private byte[] glyphSizes;
    private boolean glyphTried;
    private String busy;

    public static FontPack get() {
        try {
            if (BGSConfig.useTtfFont.get()) {
                return new FontPack();
            }
        } catch (Throwable ignored) {
        }
        SS = 1;
        return null;
    }

    private FontPack() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft().gameSettings,  Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        int factor = Math.round(sr.getScaleFactor() / 2.0F);
        factor = MathHelper.clamp_int(factor, 2, MAX_SS);
        this.ss = factor;
        this.res = 256 * factor;
        this.cell = this.res / 16;
        this.pad = 1;
        this.rasterSize = this.cell * SUPER;
        this.raster = new BufferedImage(this.rasterSize, this.rasterSize, BufferedImage.TYPE_INT_ARGB);
        this.rasterGraphics = this.raster.createGraphics();
        this.rasterPixels = new int[this.rasterSize * this.rasterSize];
        this.cellPixels = new int[this.cell * this.cell];
    }

    private boolean ensureFont() {
        if (!this.fontTried) {
            this.fontTried = true;
            Font base = loadPackFont();
            if (base == null) {
                SS = 1;
                return false;
            }

            BufferedImage probeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D probe = probeImage.createGraphics();
            FontMetrics metrics = probe.getFontMetrics(base.deriveFont(100.0F));
            int span = metrics.getAscent() + metrics.getDescent();
            float size = span > 0 ? 100.0F * (this.cell - 1) / (float) span : 100.0F;
            this.font = base.deriveFont(Math.max(size, 1.0F));
            FontMetrics scaled = probe.getFontMetrics(this.font);
            this.baseline = this.cell - scaled.getDescent();
            probe.dispose();

            this.rasterFont = this.font.deriveFont(this.font.getSize2D() * SUPER);
            applyHints(this.rasterGraphics);
            this.rasterGraphics.setFont(this.rasterFont);
            this.rasterGraphics.setColor(Color.WHITE);
            SS = this.ss;
            Minecraft.getMinecraft().getLogAgent().logInfo("Using TTF font from resourcepack " + PROVIDER_PATH + " (atlas " + this.res + ")");
        }
        return this.font != null;
    }

    private static void applyHints(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
    }

    private static Font loadPackFont() {
        Set<String> domains = new LinkedHashSet<>();
        domains.add("minecraft");
        try {
            domains.addAll(Minecraft.getMinecraft().getResourceManager().getResourceDomains());
        } catch (Throwable ignored) {
        }
        for (String domain : domains) {
            List<Resource> resources;
            try {
                resources = Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation(domain, PROVIDER_PATH, false));
            } catch (Exception exception) {
                continue;
            }
            if (resources == null) {
                continue;
            }
            for (int i = resources.size() - 1; i >= 0; i--) {
                try {
                    byte[] json = readAll(resources.get(i).getInputStream());
                    if (json == null) {
                        continue;
                    }
                    for (String reference : parseProviders(new String(json, StandardCharsets.UTF_8))) {
                        Font font = loadPackTtf(reference);
                        if (font != null) {
                            return font;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private static List<String> parseProviders(String json) {
        List<String> files = new ArrayList<String>();
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        JsonArray providers = root.getAsJsonArray("providers");
        if (providers == null) {
            return files;
        }
        for (JsonElement element : providers) {
            JsonObject provider = element.getAsJsonObject();
            JsonElement type = provider.get("type");
            JsonElement file = provider.get("file");
            if (type == null || file == null || !"ttf".equals(type.getAsString()) || !file.isJsonPrimitive()) {
                continue;
            }
            files.add(file.getAsString());
        }
        return files;
    }

    private static Font loadPackTtf(String reference) {
        String domain = "minecraft";
        String path = reference;
        int colon = reference.indexOf(58);
        if (colon > 0) {
            domain = reference.substring(0, colon);
            path = reference.substring(colon + 1);
        }
        String[] candidates = {"font/" + path, "font/" + path + ".ttf", "font/" + path + ".otf"};
        for (String candidate : candidates) {
            try {
                Resource resource = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(domain, candidate, false));
	            try (InputStream in = resource.getInputStream()) {
		            return Font.createFont(Font.TRUETYPE_FONT, in);
	            }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) {
        byte[] data = this.data(location);
        return new ByteArrayInputStream(data == null ? new byte[0] : data);
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return this.data(location) != null;
    }

    private byte[] data(ResourceLocation location) {
        String path = location.getResourcePath();
        if (path.equals(this.busy) || !this.ensureFont()) {
            return null;
        }
        if (path.endsWith(META_SUFFIX)) {
            String base = path.substring(0, path.length() - META_SUFFIX.length());
            return this.claimed(base) ? SMOOTH : null;
        }
        if (ASCII_PATH.equals(path)) {
            return this.ascii();
        }
        if (GLYPH_PATH.equals(path)) {
            if (!this.glyphTried) {
                this.glyphTried = true;
                this.glyphSizes = this.buildGlyphSizes();
            }
            return this.glyphSizes;
        }
        int page = pageOf(path);
        return page < 0 ? null : this.page(page);
    }

    private boolean claimed(String base) {
        if (ASCII_PATH.equals(base)) {
            return this.ascii() != null;
        }
        int page = pageOf(base);
        return page >= 0 && this.pageWanted(page);
    }

    private byte[] ascii() {
        if (!this.asciiTried) {
            this.asciiTried = true;
            this.asciiPng = this.buildAscii();
        }
        return this.asciiPng;
    }

    private byte[] page(int page) {
        if (this.emptyPages.contains(page)) {
            return null;
        }
        byte[] cached = this.pages.get(page);
        if (cached == null) {
            cached = this.buildPage(page);
            if (cached == null) {
                this.emptyPages.add(page);
            } else {
                this.pages.put(page, cached);
            }
        }
        return cached;
    }

    private boolean pageWanted(int page) {
        for (int i = 0; i < 256; i++) {
            if (this.font.canDisplay((char) (page * 256 + i))) {
                return true;
            }
        }
        return false;
    }

    private static int pageOf(String path) {
        if (!path.startsWith(PAGE_PREFIX) || !path.endsWith(".png")) {
            return -1;
        }
        String hex = path.substring(PAGE_PREFIX.length(), path.length() - 4);
        if (hex.length() != 2) {
            return -1;
        }
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private byte[] buildAscii() {
        BufferedImage image = new BufferedImage(this.res, this.res, BufferedImage.TYPE_INT_ARGB);
        int drawn = 0;
        for (int i = 0; i < 256 && i < ASCII.length(); i++) {
            char ch = ASCII.charAt(i);
            if (ch == 0 || !this.font.canDisplay(ch)) {
                continue;
            }
            int[] pixels = this.renderCell(ch);
            if (pixels == null) {
                continue;
            }
            image.setRGB(i % 16 * this.cell, i / 16 * this.cell, this.cell, this.cell, pixels, 0, this.cell);
            drawn++;
        }
        return drawn == 0 ? null : toPng(image);
    }

    private byte[] buildPage(int page) {
        BufferedImage image = this.readBaselinePage(page);
        boolean hasBaseline = image != null;
        if (!hasBaseline) {
            image = new BufferedImage(this.res, this.res, BufferedImage.TYPE_INT_ARGB);
        }
        int[] blank = new int[this.cell * this.cell];
        int drawn = 0;
        for (int i = 0; i < 256; i++) {
            char ch = (char) (page * 256 + i);
            if (!this.font.canDisplay(ch)) {
                continue;
            }
            int[] pixels = this.renderCell(ch);
            if (pixels == null) {
                continue;
            }
            int cellX = i % 16 * this.cell;
            int cellY = i / 16 * this.cell;
            if (hasBaseline) {
                image.setRGB(cellX, cellY, this.cell, this.cell, blank, 0, this.cell);
            }
            image.setRGB(cellX, cellY, this.cell, this.cell, pixels, 0, this.cell);
            drawn++;
        }
        if (drawn == 0 && (!hasBaseline || !this.pageWanted(page))) {
            return null;
        }
        return toPng(image);
    }

    private byte[] buildGlyphSizes() {
        byte[] out = new byte[65536];
        byte[] base = this.readBaseline(GLYPH_PATH);
        if (base != null) {
            System.arraycopy(base, 0, out, 0, Math.min(base.length, out.length));
        }
        for (int c = 32; c < out.length; c++) {
            if (c >= 0xD800 && c <= 0xDFFF) {
                continue;
            }
            int metric = this.measure((char) c);
            if (metric >= 0) {
                out[c] = (byte) metric;
            }
        }
        return out;
    }

    private int[] renderCell(char ch) {
        this.rasterGraphics.setComposite(AlphaComposite.Clear);
        this.rasterGraphics.fillRect(0, 0, this.rasterSize, this.rasterSize);
        this.rasterGraphics.setComposite(AlphaComposite.SrcOver);
        this.rasterGraphics.drawString(String.valueOf(ch), this.pad * SUPER, this.baseline * SUPER);
        this.raster.getRGB(0, 0, this.rasterSize, this.rasterSize, this.rasterPixels, 0, this.rasterSize);
        int[] out = this.cellPixels;
        int window = SUPER * SUPER;
        boolean ink = false;
        for (int y = 0; y < this.cell; y++) {
            int row = y * this.cell;
            int sourceRow = y * SUPER * this.rasterSize;
            for (int x = 0; x < this.cell; x++) {
                int source = sourceRow + x * SUPER;
                int alpha = 0;
                for (int j = 0; j < SUPER; j++) {
                    int offset = source + j * this.rasterSize;
                    for (int i = 0; i < SUPER; i++) {
                        alpha += this.rasterPixels[offset + i] >>> 24;
                    }
                }
                alpha /= window;
                if (alpha == 0) {
                    out[row + x] = 0;
                } else {
                    out[row + x] = alpha << 24 | 0xFFFFFF;
                    ink = true;
                }
            }
        }
        return ink ? out : null;
    }

    private int measure(char ch) {
        if (ch == 0 || !this.font.canDisplay(ch)) {
            return -1;
        }
        int[] pixels = this.renderCell(ch);
        if (pixels == null) {
            return -1;
        }
        int min = this.cell;
        int max = -1;
        for (int y = 0; y < this.cell; y++) {
            int row = y * this.cell;
            for (int x = 0; x < this.cell; x++) {
                if ((pixels[row + x] >>> 24) == 0) {
                    continue;
                }
                if (x < min) {
                    min = x;
                }
                if (x > max) {
                    max = x;
                }
            }
        }
        if (max < 0) {
            return -1;
        }
        int left = Math.min(min / this.ss, 7);
        int right = max / this.ss;
        return (left << 4) | right;
    }

    private BufferedImage readBaselinePage(int page) {
        byte[] data = this.readBaseline(String.format("textures/font/unicode_page_%02x.png", page));
        if (data == null) {
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
            if (image == null || image.getWidth() != 256 || image.getHeight() != 256) {
                return null;
            }
            return upscale(image);
        } catch (IOException exception) {
            return null;
        }
    }

    private BufferedImage upscale(BufferedImage source) {
        BufferedImage image = new BufferedImage(this.res, this.res, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(source, 0, 0, this.res, this.res, null);
        graphics.dispose();
        return image;
    }

    private byte[] readBaseline(String path) {
        String previous = this.busy;
        this.busy = path;
        try {
            List resources = Minecraft.getMinecraft().getResourceManager().getAllResources(new ResourceLocation("minecraft", path, false));
            if (resources == null || resources.isEmpty()) {
                return null;
            }
            Resource resource = (Resource) resources.get(resources.size() - 1);
            return readAll(resource.getInputStream());
        } catch (Exception exception) {
            return null;
        } finally {
            this.busy = previous;
        }
    }

    private static byte[] readAll(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }
	    try (in) {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    byte[] buffer = new byte[8192];
		    int read;
		    while ((read = in.read(buffer)) != -1) {
			    out.write(buffer, 0, read);
		    }
		    return out.toByteArray();
	    }
    }

    private static byte[] toPng(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException exception) {
            return null;
        }
    }

    @Override
    public Set getResourceDomains() {
        return Minecraft.getMinecraft().getResourceManager().getResourceDomains();
    }

    @Override
    public MetadataSection getPackMetadata(MetadataSerializer serializer, String section) {
        return null;
    }

    @Override
    public BufferedImage getPackImage() {
        return null;
    }

    @Override
    public String getPackName() {
        return "BGS TTF Font";
    }
}
