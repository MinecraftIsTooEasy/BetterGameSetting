package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import net.minecraft.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;

@Mixin(GuiSlot.class)
public abstract class GuiSlotMixin implements IGuiSlot {
    @Shadow public int width;
    @Shadow protected int top;
    @Shadow protected int bottom;
    @Shadow public int right;
    @Shadow public int left;
    @Shadow protected int mouseX;
    @Shadow public float amountScrolled;
    @Shadow public int field_77242_t;
    @Shadow protected int mouseY;
    @Shadow private int selectedElement;
    @Shadow private float initialClickY;
    @Shadow private float scrollMultiplier;
    @Shadow private long lastClicked;
    @Shadow public boolean field_77243_s;
    @Shadow public boolean showSelectionBox;
    @Shadow private int height;
    @Shadow @Final private Minecraft mc;
    @Shadow @Final @Mutable protected final int slotHeight;
    @Shadow protected abstract void elementClicked(int var1, boolean var2);
    @Shadow protected abstract int getSize();
    @Shadow protected abstract int getContentHeight();
    @Shadow protected abstract void func_77224_a(int par1, int par2);
    @Shadow public abstract int func_77209_d();
    @Shadow protected abstract void drawBackground();
    @Shadow public abstract void bindAmountScrolled();
    @Shadow protected abstract boolean isSelected(int var1);
    @Shadow protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);
    @Shadow protected abstract void func_77222_a(int par1, int par2, Tessellator par3Tessellator);
    @Shadow protected abstract void overlayBackground(int par1, int par2, int par3, int par4);
    @Shadow protected abstract void func_77215_b(int par1, int par2);
    @Shadow public abstract void drawDarkenedBackground(int layer);

    @Unique private int listWidth = 220;

    public GuiSlotMixin(int slotHeight) {
        this.slotHeight = slotHeight;
    }

    @Override
    public boolean isMouseYWithinSlotBounds(int mouseY) {
        return mouseY >= this.top && mouseY <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    @Override
    public int getSlotIndexFromScreenCoords(int mouseX, int mouseY) {
        int i = this.left + this.width / 2 - this.getListWidth() / 2;
        int j = this.left + this.width / 2 + this.getListWidth() / 2;
        int k = mouseY - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
        int l = k / this.slotHeight;
        return mouseX < this.getScrollBarX() && mouseX >= i && mouseX <= j && l >= 0 && k >= 0 && l < 88888 ? l : -1;
    }

    @Override
    public int getScrollBarX() {
        return this.width / 2 + 124;
    }

    @Override
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                int i = (this.width - this.getListWidth()) / 2;
                int j = (this.width + this.getListWidth()) / 2;
                int k = this.mouseY - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
                int l = k / this.slotHeight;

                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
                    this.elementClicked(l, false);
                    this.selectedElement = l;
                } else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
                    this.func_77224_a(this.mouseX - i, this.mouseY - this.top + (int) this.amountScrolled - 4);
                }
            }

            if (Mouse.isButtonDown(0)) {
                if (this.initialClickY != -1) {
                    if (this.initialClickY >= 0) {
                        this.amountScrolled -= (float) (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                        this.initialClickY = this.mouseY;
                    }
                } else {
                    boolean flag1 = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        int j2 = (this.width - this.getListWidth()) / 2;
                        int k2 = (this.width + this.getListWidth()) / 2;
                        int l2 = this.mouseY - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
                        int i1 = l2 / this.slotHeight;

                        if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
                            boolean flag = i1 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(i1, flag);
                            this.selectedElement = i1;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.func_77224_a(this.mouseX - j2, this.mouseY - this.top + (int) this.amountScrolled - 4);
                            flag1 = false;
                        }

                        int i3 = this.getScrollBarX();
                        int j1 = i3 + 6;

                        if (this.mouseX >= i3 && this.mouseX <= j1) {
                            this.scrollMultiplier = -1.0F;
                            int k1 = this.func_77209_d();

                            if (k1 < 1) {
                                k1 = 1;
                            }

                            int l1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());
                            l1 = MathHelper.clamp_int(l1, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float) (this.bottom - this.top - l1) / (float) k1;
                        } else {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1) {
                            this.initialClickY = this.mouseY;
                        } else {
                            this.initialClickY = -2;
                        }
                    } else {
                        this.initialClickY = -2;
                    }
                }
            } else {
                this.initialClickY = -1;
            }
//            while (this.mc.gameSettings.touchscreen && Mouse.next()) {
            int i2 = Mouse.getEventDWheel();
//                if (i2 == 0) continue;
            if (i2 > 0) {
                i2 = -1;
            } else if (i2 < 0) {
                i2 = 1;
            }
            this.amountScrolled += (float) (i2 * this.slotHeight / 2);
//            }
        }
    }

    @Override
    public void setSlotXBoundsFromLeft(int left) {
        this.left = left;
        this.right = left + this.width;
    }

    @Override
    public int getListWidth() {
        return this.listWidth;
    }

    @Override
    public void setListWidth(int listWidth) {
        this.listWidth = listWidth;
    }


    /**
     * @author Xy_Lose
     * @reason Modern drawScreen
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float p_148128_3_) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();
        int k = this.getSize();
        int l = this.getScrollBarX();
        int i1 = l + 6;
        int l1;
        int i2;
        int k2;
        int i3;

        if (mouseX > this.left && mouseX < this.right && mouseY > this.top && mouseY < this.bottom) {
            if (Mouse.isButtonDown(0)) {
                if (this.initialClickY == -1.0F) {
                    boolean flag1 = true;

                    if (mouseY >= this.top && mouseY <= this.bottom) {
                        int k1 = this.width / 2 - this.getListWidth() / 2;
                        l1 = this.width / 2 + this.getListWidth() / 2;
                        i2 = mouseY - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
                        int j2 = i2 / this.slotHeight;

                        if (mouseX >= k1 && mouseX <= l1 && j2 >= 0 && i2 >= 0 && j2 < k) {
                            boolean flag = j2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(j2, flag);
                            this.selectedElement = j2;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (mouseX >= k1 && mouseX <= l1 && i2 < 0) {
                            this.func_77224_a(mouseX - k1, mouseY - this.top + (int) this.amountScrolled - 4);
                            flag1 = false;
                        }

                        if (mouseX >= l && mouseX <= i1) {
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
                            this.initialClickY = (float) mouseY;
                        } else {
                            this.initialClickY = -2.0F;
                        }
                    } else {
                        this.initialClickY = -2.0F;
                    }
                } else if (this.initialClickY >= 0.0F) {
                    this.amountScrolled -= ((float) mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = (float) mouseY;
                }
            } else {
                for (; !this.mc.gameSettings.touchscreen && Mouse.next(); this.mc.currentScreen.handleMouseInput()) {
                    int j1 = Mouse.getEventDWheel();

                    if (j1 != 0) {
                        if (j1 > 0) {
                            j1 = -1;
                        } else if (j1 < 0) {
                            j1 = 1;
                        }

                        this.amountScrolled += (float) (j1 * this.slotHeight / 2);
                    }
                }

                this.initialClickY = -1.0F;
            }
        }

        this.bindAmountScrolled();
        this.drawDarkenedBackground(1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        drawContainerBackground(tessellator);
        l1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        i2 = this.top + 4 - (int) this.amountScrolled;

        if (this.field_77243_s) {
            this.func_77222_a(l1, i2, tessellator);
        }

        this.drawSelectionBox(l1, i2, mouseX, mouseY);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double) this.left, (double) (this.top + b0), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double) this.right, (double) (this.top + b0), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double) this.right, (double) this.top, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double) this.left, (double) this.top, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV((double) this.left, (double) this.bottom, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV((double) this.right, (double) this.bottom, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV((double) this.right, (double) (this.bottom - b0), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV((double) this.left, (double) (this.bottom - b0), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
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
            tessellator.addVertexWithUV((double) l, (double) this.bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double) i1, (double) this.bottom, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double) i1, (double) this.top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) l, (double) this.top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV((double) l, (double) (l2 + k2), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double) i1, (double) (l2 + k2), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double) i1, (double) l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) l, (double) l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV((double) l, (double) (l2 + k2 - 1), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV((double) (i1 - 1), (double) (l2 + k2 - 1), 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV((double) (i1 - 1), (double) l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV((double) l, (double) l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }

        this.func_77215_b(mouseX, mouseY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Unique
    protected void drawSelectionBox(int x, int y, int mouseX, int mouseY) {
        int i1 = this.getSize();
        Tessellator tessellator = Tessellator.instance;

        for (int j1 = 0; j1 < i1; ++j1) {
            int k1 = y + j1 * this.slotHeight + this.field_77242_t;
            int l1 = this.slotHeight - 4;

            if (k1 <= this.bottom && k1 + l1 >= this.top) {
                if (this.showSelectionBox && this.isSelected(j1)) {
                    int i2 = this.left + (this.width / 2 - this.getListWidth() / 2);
                    int j2 = this.left + this.width / 2 + this.getListWidth() / 2;
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    tessellator.setColorOpaque_I(8421504);
                    tessellator.addVertexWithUV((double) i2, (double) (k1 + l1 + 2), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double) j2, (double) (k1 + l1 + 2), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double) j2, (double) (k1 - 2), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double) i2, (double) (k1 - 2), 0.0D, 0.0D, 0.0D);
                    tessellator.setColorOpaque_I(0);
                    tessellator.addVertexWithUV((double) (i2 + 1), (double) (k1 + l1 + 1), 0.0D, 0.0D, 1.0D);
                    tessellator.addVertexWithUV((double) (j2 - 1), (double) (k1 + l1 + 1), 0.0D, 1.0D, 1.0D);
                    tessellator.addVertexWithUV((double) (j2 - 1), (double) (k1 - 1), 0.0D, 1.0D, 0.0D);
                    tessellator.addVertexWithUV((double) (i2 + 1), (double) (k1 - 1), 0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(j1, x, k1, l1, tessellator);
            }
        }
    }

    @Unique
    protected void drawContainerBackground(Tessellator tessellator) {
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f1 = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(2105376);
        tessellator.addVertexWithUV((double)this.left, (double)this.bottom, 0.0D, (double)((float)this.left / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV((double)this.right, (double)this.bottom, 0.0D, (double)((float)this.right / f1), (double)((float)(this.bottom + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV((double)this.right, (double)this.top, 0.0D, (double)((float)this.right / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        tessellator.addVertexWithUV((double)this.left, (double)this.top, 0.0D, (double)((float)this.left / f1), (double)((float)(this.top + (int)this.amountScrolled) / f1));
        tessellator.draw();
    }
}
