package moddedmite.xylose.bettergamesetting.client.gui.base;

import net.minecraft.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlotModern {
    private final Minecraft client;
    public int width;
    public int height;
    public int top;
    public int bottom;
    public int right;
    public int left;
    public final int slotHeight;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    protected int mouseX;
    protected int mouseY;
    protected boolean field_148163_i = true;
    private float initialClickY = -2.0F;
    private float scrollMultiplier;
    private float amountScrolled;
    private int selectedElement = -1;
    private long lastClicked;
    private boolean showSelectionBox = true;
    private boolean hasListHeader;
    public int headerPadding;
    private boolean enabled = true;

    public GuiSlotModern(Minecraft client, int width, int height, int top, int bottom, int slotHeight) {
        this.client = client;
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = slotHeight;
        this.left = 0;
        this.right = width;
    }

    public void resetSize(int width, int height, int top, int bottom) {
        this.width = width;
        this.height = height;
        this.top = top;
        this.bottom = bottom;
        this.left = 0;
        this.right = width;
    }

    public void setShowSelectionBox(boolean bl) {
        this.showSelectionBox = bl;
    }

    /**
     * Sets hasListHeader and headerHeight. Params: hasListHeader, headerHeight. If hasListHeader is false headerHeight
     * is set to 0.
     */
    protected void setHasListHeader(boolean hasListHeader, int headerHeight) {
        this.hasListHeader = hasListHeader;
        this.headerPadding = headerHeight;

        if (!hasListHeader) {
            this.headerPadding = 0;
        }
    }

    protected abstract int getSize();

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected abstract void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY);

    /**
     * Returns true if the element passed in is currently selected
     */
    protected abstract boolean isSelected(int slotIndex);

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(int slotIndex, int x, int y, int slotHeight, Tessellator tessellator, int mouseX, int mouseY);

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int x, int y, Tessellator tessellator) {
    }

    protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
    }

    protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
    }

    public int getSlotIndexFromScreenCoords(int mouseX, int mouseY) {
        int k = this.left + this.width / 2 - this.getListWidth() / 2;
        int l = this.left + this.width / 2 + this.getListWidth() / 2;
        int i = mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
        int j = i / this.slotHeight;
        return mouseX < this.getScrollBarX() && mouseX >= k && mouseX <= l && j >= 0 && i >= 0 && j < this.getSize() ? j : -1;
    }

    /**
     * Registers the IDs that can be used for the scrollbar's up/down buttons.
     */
    public void registerScrollButtons(int upButtonId, int downButtonId) {
        this.scrollUpButtonID = upButtonId;
        this.scrollDownButtonID = downButtonId;
    }

    /**
     * Stop the thing from scrolling out of bounds
     */
    private void bindAmountScrolled() {
        int i = this.getViewableHeight();

        if (i < 0) {
            i /= 2;
        }

        if (!this.field_148163_i && i < 0) {
            i = 0;
        }

        if (this.amountScrolled < 0.0F) {
            this.amountScrolled = 0.0F;
        }

        if (this.amountScrolled > (float) i) {
            this.amountScrolled = (float) i;
        }
    }

    public int getViewableHeight() {
        return this.getContentHeight() - (this.bottom - this.top - 4);
    }

    /**
     * Returns the amountScrolled field as an integer.
     */
    public int getAmountScrolled() {
        return (int) this.amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int mouseY) {
        return mouseY >= this.top && mouseY <= this.bottom;
    }

    /**
     * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
     */
    public void scrollBy(int amount) {
        this.amountScrolled += (float) amount;
        this.bindAmountScrolled();
        this.initialClickY = -2.0F;
    }

    public void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float) (this.slotHeight * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            } else if (button.id == this.scrollDownButtonID) {
                this.amountScrolled += (float) (this.slotHeight * 2 / 3);
                this.initialClickY = -2.0F;
                this.bindAmountScrolled();
            }
        }
    }

    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        ScaledResolution scaledresolution = new ScaledResolution(client.gameSettings, client.displayWidth, client.displayHeight);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int mouseXR = Mouse.getX() * width / client.displayWidth;
        int mouseYR = height - Mouse.getY() * height / client.displayHeight - 1;
        this.mouseX = mouseXR;
        this.mouseY = mouseYR;
        this.drawBackground();
        int k = this.getSize();
        int l = this.getScrollBarX();
        int i1 = l + 6;
        int l1;
        int i2;
        int k2;
        int i3;

        if (mouseXR > this.left && mouseXR < this.right && mouseYR > this.top && mouseYR < this.bottom) {
            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                if (this.initialClickY == -1.0F) {
                    boolean flag1 = true;

                    if (mouseYR >= this.top && mouseYR <= this.bottom) {
                        int k1 = this.width / 2 - this.getListWidth() / 2;
                        l1 = this.width / 2 + this.getListWidth() / 2;
                        i2 = mouseYR - this.top - this.headerPadding + (int) this.amountScrolled - 4;
                        int j2 = i2 / this.slotHeight;

                        if (mouseXR >= k1 && mouseXR <= l1 && j2 >= 0 && i2 >= 0 && j2 < k) {
                            boolean flag = j2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(j2, flag, mouseXR, mouseYR);
                            this.selectedElement = j2;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (mouseXR >= k1 && mouseXR <= l1 && i2 < 0) {
                            this.func_148132_a(mouseXR - k1, mouseYR - this.top + (int) this.amountScrolled - 4);
                            flag1 = false;
                        }

                        if (mouseXR >= l && mouseXR <= i1) {
                            this.scrollMultiplier = -1.0F;
                            i3 = this.getViewableHeight();

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
                            this.initialClickY = (float) mouseYR;
                        } else {
                            this.initialClickY = -2.0F;
                        }
                    } else {
                        this.initialClickY = -2.0F;
                    }
                } else if (this.initialClickY >= 0.0F) {
                    this.amountScrolled -= ((float) mouseYR - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = (float) mouseYR;
                }
            } else {
                while (!this.client.gameSettings.touchscreen && Mouse.next()) {
                    int j1 = Mouse.getEventDWheel();

                    if (j1 != 0) {
                        if (j1 > 0) {
                            j1 = -1;
                        } else if (j1 < 0) {
                            j1 = 1;
                        }

                        this.amountScrolled += (float) (j1 * this.slotHeight / 2);
                    }
                    this.client.currentScreen.handleMouseInput();
                }

                this.initialClickY = -1.0F;
            }
        }

        this.bindAmountScrolled();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        drawContainerBackground(tessellator);
        ScaledResolution sr = new ScaledResolution(this.client.gameSettings, this.client.displayWidth, this.client.displayHeight);
        GL11.glScissor((this.left * sr.getScaleFactor()), (this.client.displayHeight - this.bottom * sr.getScaleFactor()), ((this.right - this.left) * sr.getScaleFactor()), ((this.bottom - this.top) * sr.getScaleFactor()));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        l1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        i2 = this.top + 4 - (int) this.amountScrolled;

        if (this.hasListHeader) {
            this.drawListHeader(l1, i2, tessellator);
        }

        this.drawSelectionBox(l1, i2, mouseXR, mouseYR);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // draw top and bottom block gradient matte
        if (!client.gameSettings.isTransparentBackground()) {
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
            tessellator.addVertexWithUV(this.left, this.bottom - b0, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }
        i3 = this.getViewableHeight();

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
            tessellator.addVertexWithUV(l, l2 + k2, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1, l2 + k2, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1, l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV(l, (l2 + k2 - 1), 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1 - 1, l2 + k2 - 1, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1 - 1, l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }

        this.func_148142_b(mouseXR, mouseYR);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void setEnabled(boolean enabledIn) {
        this.enabled = enabledIn;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return 220;
    }

    /**
     * Draws the selection box around the selected slot element.
     */
    protected void drawSelectionBox(int x, int y, int mouseXIn, int mouseYIn) {
        int i1 = this.getSize();
        Tessellator tessellator = Tessellator.instance;

        for (int j1 = 0; j1 < i1; ++j1) {
            int k1 = y + j1 * this.slotHeight + this.headerPadding;
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

                this.drawSlot(j1, x, k1, l1, tessellator, mouseXIn, mouseYIn);
            }
        }
    }

    protected int getScrollBarX() {
        return this.width / 2 + 124;
    }

    /**
     * Overlays the background to hide scrolled items
     */
    private void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        if (client.gameSettings.isTransparentBackground()) return;
        Tessellator tessellator = Tessellator.instance;
        this.client.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(4210752, endAlpha);
        tessellator.addVertexWithUV(this.left, endY, 0.0D, 0.0D, ((float) endY / f));
        tessellator.addVertexWithUV((this.left + this.width), endY, 0.0D, ((float) this.width / f), ((float) endY / f));
        tessellator.setColorRGBA_I(4210752, startAlpha);
        tessellator.addVertexWithUV((this.left + this.width), startY, 0.0D, ((float) this.width / f), ((float) startY / f));
        tessellator.addVertexWithUV(this.left, startY, 0.0D, 0.0D, ((float) startY / f));
        tessellator.draw();
    }

    /**
     * Sets the left and right bounds of the slot. Param is the left bound, right is calculated as left + width.
     */
    public void setSlotXBoundsFromLeft(int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }

    public int getSlotHeight() {
        return this.slotHeight;
    }

    protected void drawContainerBackground(Tessellator tessellator) {
        if (client.gameSettings.isTransparentBackground()) {
            Gui.drawRect(this.left, this.top, this.right, this.bottom, 0x66000000);//draw slot dark background
            //draw slot frame line
            Gui.drawRect(this.left, this.top, this.right, this.top - 1, 0xCC000000);
            Gui.drawRect(this.left, this.bottom, this.right, this.bottom + 1, 0xCC000000);
            Gui.drawRect(this.left, this.top - 1, this.right, this.top - 2, 0x66ADB1B1);
            Gui.drawRect(this.left, this.bottom + 1, this.right, this.bottom + 2, 0x66ADB1B1);
        } else {
            this.client.getTextureManager().bindTexture(Gui.optionsBackground);
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
    }
}