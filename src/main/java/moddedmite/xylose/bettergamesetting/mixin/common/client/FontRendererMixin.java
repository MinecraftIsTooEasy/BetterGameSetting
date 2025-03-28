package moddedmite.xylose.bettergamesetting.mixin.common.client;

import de.thexxturboxx.fontfixer.FontFixer;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Shadow private float red;
    @Shadow private float blue;
    @Shadow private float green;
    @Shadow private float alpha;
    @Shadow private boolean randomStyle;
    @Shadow private boolean boldStyle;
    @Shadow private boolean strikethroughStyle;
    @Shadow private boolean underlineStyle;
    @Shadow private boolean italicStyle;
    @Shadow private int textColor;
    @Shadow public Random fontRandom;
    @Shadow private int[] charWidth;
    @Shadow private float posX;
    @Shadow private float posY;
    @Shadow public int FONT_HEIGHT;
    @Shadow protected abstract float renderDefaultChar(int var1, boolean var2);
    @Shadow protected abstract float renderUnicodeChar(char var1, boolean var2);

    @Unique private final int[] colorCode = new int[32];
    @Unique private final String ASCII = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";


    @Unique private static final Map<FontRenderer, FontFixer> registeredFixers = new HashMap();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init2(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4, CallbackInfo ci) {
        registeredFixers.put(ReflectHelper.dyCast(this), new FontFixer(par1GameSettings, par2ResourceLocation, par3TextureManager, par4));
    }

    @ModifyConstant(method = "<init>", constant = {@Constant(intValue = 256)})
    private int modifyChanceTableSize(int val) {
        return Short.MAX_VALUE;
    }

    @Overwrite
    public int drawStringWithShadow(String text, int x, int y, int color) {
        return (registeredFixers.get(ReflectHelper.dyCast(this))).drawStringWithShadow(text, x, y, color);
    }

    @Overwrite
    public int drawString(String string, int i, int j, int k) {
        return (registeredFixers.get(ReflectHelper.dyCast(this))).drawString(string, i, j, k);
    }

    @Overwrite
    public int getStringWidth(String text) {
        return (registeredFixers.get(ReflectHelper.dyCast(this))).getStringWidth(text);
    }

    @Overwrite
    public int getCharWidth(char c) {
        return (registeredFixers.get(ReflectHelper.dyCast(this))).getCharWidth(c);
    }

//    @Overwrite
//    private void renderStringAtPos(String par1Str, boolean par2) {
//        (registeredFixers.get(ReflectHelper.dyCast(this))).renderStringAtPos(par1Str, par2);
//    }

//    @Overwrite
//    public String trimStringToWidth(String text, int width) {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).trimStringToWidth(text, width);
//    }
//
//    @Overwrite
//    public String trimStringToWidth(String text, int width, boolean bl) {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).trimStringToWidth(text, width, bl);
//    }
//
//    @Overwrite
//    public void drawSplitString(String text, int x, int y, int maxWidth, int color) {
//        (registeredFixers.get(ReflectHelper.dyCast(this))).drawSplitString(text, x, y, maxWidth, color);
//    }
//
//    @Overwrite
//    public int splitStringWidth(String string, int i) {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).splitStringWidth(string, i);
//    }
//
//    @Overwrite
//    public void setUnicodeFlag(boolean bl) {
//        (registeredFixers.get(ReflectHelper.dyCast(this))).setUnicodeFlag(bl);
//    }

    @Overwrite
    public void setBidiFlag(boolean rightToLeft) {
        (registeredFixers.get(ReflectHelper.dyCast(this))).setBidiFlag(rightToLeft);
    }

//    @Overwrite
//    public List listFormattedStringToWidth(String text, int width) {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).listFormattedStringToWidth(text, width);
//    }

//    @Overwrite
//    public String wrapFormattedStringToWidth(String text, int width) {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).wrapFormattedStringToWidth(text, width);
//    }
//
//    @Overwrite
//    public boolean getBidiFlag() {
//        return (registeredFixers.get(ReflectHelper.dyCast(this))).getBidiFlag();
//    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setColor(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4, CallbackInfo ci) {
        for (int var5 = 0; var5 < 32; ++var5) {
            int var6 = (var5 >> 3 & 1) * 85;
            int var7 = (var5 >> 2 & 1) * 170 + var6;
            int var8 = (var5 >> 1 & 1) * 170 + var6;
            int var9 = (var5 >> 0 & 1) * 170 + var6;
            if (var5 == 6) {
                var7 += 85;
            }

            if (par1GameSettings.anaglyph) {
                int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
                int var11 = (var7 * 30 + var8 * 70) / 100;
                int var12 = (var7 * 30 + var9 * 70) / 100;
                var7 = var10;
                var8 = var11;
                var9 = var12;
            }

            if (var5 >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }

            this.colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
        }

    }

    @Inject(method = "renderStringAtPos", at = @At("HEAD"), cancellable = true)
    private void newRender(String string, boolean shadow, CallbackInfo ci) {
        if (((IGameSetting) Minecraft.getMinecraft().gameSettings).isForceUnicodeFont()) return;
        ci.cancel();

        for (int var3 = 0; var3 < string.length(); ++var3) {
            char var4 = string.charAt(var3);
            int var5;
            int var6;
            if (var4 == 167 && var3 + 1 < string.length()) {
                var5 = "0123456789abcdefklmnor".indexOf(string.toLowerCase().charAt(var3 + 1));
                if (var5 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (var5 < 0 || var5 > 15) {
                        var5 = 15;
                    }

                    if (shadow) {
                        var5 += 16;
                    }

                    var6 = this.colorCode[var5];
                    this.textColor = var6;
                    GL11.glColor4f((float) (var6 >> 16) / 255.0F, (float) (var6 >> 8 & 255) / 255.0F, (float) (var6 & 255) / 255.0F, this.alpha);
                } else if (var5 == 16) {
                    this.randomStyle = true;
                } else if (var5 == 17) {
                    this.boldStyle = true;
                } else if (var5 == 18) {
                    this.strikethroughStyle = true;
                } else if (var5 == 19) {
                    this.underlineStyle = true;
                } else if (var5 == 20) {
                    this.italicStyle = true;
                } else if (var5 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
                }

                ++var3;
            } else {
                var5 = this.ASCII.indexOf(var4);
                if (this.randomStyle && var5 != -1) {
                    do {
                        var6 = this.fontRandom.nextInt(this.charWidth.length);
                    } while (this.charWidth[var5] != this.charWidth[var6]);

                    var5 = var6;
                }

                float var11 = var5 == -1 ? 0.5F : 1.0F;
                boolean var7 = (var4 == 0 || var5 == -1) && shadow;
                if (var7) {
                    this.posX -= var11;
                    this.posY -= var11;
                }

                float var8 = this.newRenderChar(var5, var4, this.italicStyle);
                if (var7) {
                    this.posX += var11;
                    this.posY += var11;
                }

                if (this.boldStyle) {
                    this.posX += var11;
                    if (var7) {
                        this.posX -= var11;
                        this.posY -= var11;
                    }

                    this.newRenderChar(var5, var4, this.italicStyle);
                    this.posX -= var11;
                    if (var7) {
                        this.posX += var11;
                        this.posY += var11;
                    }

                    ++var8;
                }

                Tessellator var9;
                if (this.strikethroughStyle) {
                    var9 = Tessellator.instance;
                    GL11.glDisable(3553);
                    var9.startDrawingQuads();
                    var9.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0);
                    var9.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0);
                    var9.draw();
                    GL11.glEnable(3553);
                }

                if (this.underlineStyle) {
                    var9 = Tessellator.instance;
                    GL11.glDisable(3553);
                    var9.startDrawingQuads();
                    int var10 = this.underlineStyle ? -1 : 0;
                    var9.addVertex(this.posX + (float) var10, this.posY + (float) this.FONT_HEIGHT, 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) this.FONT_HEIGHT, 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0);
                    var9.addVertex(this.posX + (float) var10, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0);
                    var9.draw();
                    GL11.glEnable(3553);
                }

                this.posX += (float) ((int) var8);
            }
        }

    }

    @Unique
    private float newRenderChar(int par1, char par2, boolean par3) {
        if (par2 == ' ') {
            return 4.0F;
        } else {
            return (this.ASCII.indexOf(par2) != -1 && !((IGameSetting) Minecraft.getMinecraft().gameSettings).isForceUnicodeFont()) ? this.renderDefaultChar(par1, par3) : this.renderUnicodeChar(par2, par3);
        }
    }
}
