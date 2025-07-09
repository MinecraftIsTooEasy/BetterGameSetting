package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import net.minecraft.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiSlot.class, priority = 999)
public abstract class GuiSlotMixin implements IGuiSlot {
    @Shadow public int width;
    @Shadow protected int top;
    @Shadow protected int bottom;
    @Shadow public int right;
    @Shadow public int left;
    @Mutable @Final @Shadow protected final int slotHeight;
    @Shadow protected int mouseX;
    @Shadow public float amountScrolled;
    @Shadow public int field_77242_t;
    @Shadow protected int mouseY;
    @Shadow private int selectedElement;
    @Shadow private float initialClickY;
    @Shadow private float scrollMultiplier;
    @Shadow private long lastClicked;
    @Shadow @Final private Minecraft mc;
    @Shadow public boolean field_77243_s;
    @Shadow public boolean showSelectionBox;
    @Shadow private int height;
    @Shadow protected abstract void elementClicked(int var1, boolean var2);
    @Shadow protected abstract int getSize();
    @Shadow protected abstract int getContentHeight();
    @Shadow protected abstract void func_77224_a(int par1, int par2);
    @Shadow public abstract int func_77209_d();
    @Shadow protected abstract void drawBackground();
    @Shadow protected abstract boolean isSelected(int var1);
    @Shadow protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);
    @Shadow protected abstract void func_77222_a(int par1, int par2, Tessellator par3Tessellator);
    @Shadow protected abstract void overlayBackground(int par1, int par2, int par3, int par4);
    @Shadow protected abstract void func_77215_b(int i, int j);

    @Unique private int listWidth = 220;
    @Unique private boolean enabled = true;


    public GuiSlotMixin(int slotHeight) {
        this.slotHeight = slotHeight;
    }

    @Override
    public boolean isMouseYWithinSlotBounds(int mouseYIn) {
        return mouseYIn >= this.top && mouseYIn <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    @Override
    public int getSlotIndexFromScreenCoords(int mouseXIn, int mouseYIn) {
        int i = this.left + this.width / 2 - this.getListWidth() / 2;
        int j = this.left + this.width / 2 + this.getListWidth() / 2;
        int k = mouseYIn - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
        int l = k / this.slotHeight;
        return mouseXIn < this.getScrollBarX() && mouseXIn >= i && mouseXIn <= j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
    }

    @Override
    public int getScrollBarX() {
        return this.width / 2 + 124;
    }

    public void setSlotXBoundsFromLeft(int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }


    /**
     * @author Xy_Lose
     * @reason Modern GuiSlot
     */
    @Overwrite
    public void drawScreen(int mouseXIn, int mouseYIn, float p_148128_3_) {
        this.mouseX = mouseXIn;
        this.mouseY = mouseYIn;
        this.drawBackground();
        int k = this.getSize();
        int l = this.getScrollBarX();
        int i1 = l + 6;
        int l1;
        int i2;
        int k2;
        int i3;

        if (mouseXIn > this.left && mouseXIn < this.right && mouseYIn > this.top && mouseYIn < this.bottom) {
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY == -1.0F) {
                    boolean flag1 = true;

                    if (mouseYIn >= this.top && mouseYIn <= this.bottom) {
                        int k1 = this.width / 2 - this.getListWidth() / 2;
                        l1 = this.width / 2 + this.getListWidth() / 2;
                        i2 = mouseYIn - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
                        int j2 = i2 / this.slotHeight;

                        if (mouseXIn >= k1 && mouseXIn <= l1 && j2 >= 0 && i2 >= 0 && j2 < k) {
                            boolean flag = j2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(j2, flag);
                            this.selectedElement = j2;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (mouseXIn >= k1 && mouseXIn <= l1 && i2 < 0) {
                            this.func_77224_a(mouseXIn - k1, mouseYIn - this.top + (int) this.amountScrolled - 4);
                            flag1 = false;
                        }

                        if (mouseXIn >= l && mouseXIn <= i1) {
                            this.scrollMultiplier = -1.0F;
                            i3 = this.func_77209_d();

                            if (i3 < 1) {
                                i3 = 1;
                            }

                            k2 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());

                            if (k2 < 32) {
                                k2 = 32;
                            }

                            if (k2 > this.bottom - this.top - 8) {
                                k2 = this.bottom - this.top - 8;
                            }

                            this.scrollMultiplier /= (float) (this.bottom - this.top - k2) / (float) i3;
                        } else {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1) {
                            this.initialClickY = (float) mouseYIn;
                        } else {
                            this.initialClickY = -2.0F;
                        }
                    } else {
                        this.initialClickY = -2.0F;
                    }
                } else if (this.initialClickY >= 0.0F) {
                    this.amountScrolled -= ((float) mouseYIn - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = (float) mouseYIn;
                }
            } else {
                while (!this.mc.gameSettings.touchscreen && Mouse.next()) {
                    int j1 = Mouse.getEventDWheel();

                    if (j1 != 0) {
                        if (j1 > 0) {
                            j1 = -1;
                        } else if (j1 < 0) {
                            j1 = 1;
                        }

                        this.amountScrolled += (float) (j1 * this.slotHeight / 2);
                    }
                    this.mc.currentScreen.handleMouseInput();
                }

                this.initialClickY = -1.0F;
            }
        }

        this.bindAmountScrolled();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        drawContainerBackground(tessellator);
        l1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        i2 = this.top + 4 - (int) this.amountScrolled;

        if (this.field_77243_s) {
            this.func_77222_a(l1, i2, tessellator);
        }

        this.drawSelectionBox(l1, i2, mouseXIn, mouseYIn);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // draw top and bottom block gradient matte
        if (!((IGameSetting) mc.gameSettings).isTransparentBackground()) {
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertexWithUV(this.left, (this.top + b0), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(this.right, (this.top + b0), 0.0D, 1.0D, 1.0D);
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertexWithUV(this.right, (this.bottom - b0), 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) this.left, (double) (this.bottom - b0), 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }
        i3 = this.func_77209_d();

        if (i3 > 0) {
            k2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();

            if (k2 < 32) {
                k2 = 32;
            }

            if (k2 > this.bottom - this.top - 8) {
                k2 = this.bottom - this.top - 8;
            }

            int l2 = (int) this.amountScrolled * (this.bottom - this.top - k2) / i3 + this.top;

            if (l2 < this.top) {
                l2 = this.top;
            }

            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(l, this.bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1, this.bottom, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1, this.top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, this.top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV(l, (l2 + k2), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1, (l2 + k2), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1, l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV(l, (l2 + k2 - 1), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((i1 - 1), (l2 + k2 - 1), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((i1 - 1), l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }

        this.func_77215_b(mouseXIn, mouseYIn);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * @author Xy_Lose
     * @reason Modern Bind Amount Scrolled
     */
    @Overwrite
    public final void bindAmountScrolled() {
        int var1 = this.func_77209_d();

        if (var1 < 0) {
            var1 /= 2;
        }

        if (this.amountScrolled < 0.0F) {
            this.amountScrolled = 0.0F;
        }

        if (this.amountScrolled > (float) var1) {
            this.amountScrolled = (float) var1;
        }
    }

    protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn) {
        int i1 = this.getSize();
        Tessellator tessellator = Tessellator.instance;

        for (int j1 = 0; j1 < i1; ++j1) {
            int k1 = p_148120_2_ + j1 * this.slotHeight + this.field_77242_t;
            int l1 = this.slotHeight - 4;

            if (k1 <= this.bottom && k1 + l1 >= this.top) {
                if (this.showSelectionBox && this.isSelected(j1)) {
                    int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                    int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorOpaque_I(8421504);
                    tessellator.addVertexWithUV(i2, k1 + l1 + 2, 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(j2, k1 + l1 + 2, 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV(j2, k1 - 2, 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV(i2, k1 - 2, 0.0D, 0.0D, 0.0D);
                    tessellator.setColorOpaque_I(0);
                    tessellator.addVertexWithUV(i2 + 1, k1 + l1 + 1, 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV(j2 - 1, k1 + l1 + 1, 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV(j2 - 1, k1 - 1, 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV(i2 + 1, k1 - 1, 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(j1, p_148120_1_, k1, l1, tessellator);
            }
        }
    }

    public void setEnabled(boolean enabledIn) {
        this.enabled = enabledIn;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public int getListWidth() {
        return this.listWidth;
    }

    @Override
    public void setListWidth(int listWidthIn) {
        this.listWidth = listWidthIn;
    }

    @Unique
    protected void drawContainerBackground(Tessellator tessellator) {
        if (((IGameSetting) mc.gameSettings).isTransparentBackground()) {
            Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
        } else {
            this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float f1 = 32.0F;
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(2105376);
            tessellator.addVertexWithUV(this.left, this.bottom, 0.0D, ((float) this.left / f1), ((float) (this.bottom + (int) this.amountScrolled) / f1));
            tessellator.addVertexWithUV(this.right, this.bottom, 0.0D, ((float) this.right / f1), ((float) (this.bottom + (int) this.amountScrolled) / f1));
            tessellator.addVertexWithUV(this.right, this.top, 0.0D, ((float) this.right / f1), ((float) (this.top + (int) this.amountScrolled) / f1));
            tessellator.addVertexWithUV(this.left, this.top, 0.0D, ((float) this.left / f1), ((float) (this.top + (int) this.amountScrolled) / f1));
            tessellator.draw();
        }
        ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glScissor((this.left * sr.getScaleFactor()), (this.mc.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Inject(method = "overlayBackground", at = @At("HEAD"), cancellable = true)
    private void transparentBackground(int par1, int par2, int par3, int par4, CallbackInfo ci) {
        if (((IGameSetting) mc.gameSettings).isTransparentBackground()) {
            ci.cancel();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
