package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiSlot;
import net.minecraft.GuiSlot;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiSlot.class)
public abstract class GuiSlotMixin implements IGuiSlot {
    @Shadow public int width;
    @Shadow protected int top;
    @Shadow protected int bottom;
    @Shadow private int right;
    @Shadow public int left;
    @Shadow protected final int slotHeight;
    @Shadow protected int mouseX;
    @Shadow private float amountScrolled;
    @Shadow private int field_77242_t;
    @Shadow protected int mouseY;
    @Shadow protected abstract void elementClicked(int var1, boolean var2);
    @Shadow private int selectedElement;
    @Shadow private float initialClickY;
    @Shadow private float scrollMultiplier;
    @Shadow protected abstract int getSize();
    @Shadow private long lastClicked;
    @Shadow protected abstract int getContentHeight();
    @Shadow protected abstract void func_77224_a(int par1, int par2);
    @Shadow public abstract int func_77209_d();
    @Shadow @Final private Minecraft mc;

    public GuiSlotMixin(int slotHeight) {
        this.slotHeight = slotHeight;
    }

    @Override
    public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    @Override
    public int getSlotIndexFromScreenCoords(int p_148124_1_, int p_148124_2_) {
        int i = this.left + this.width / 2 - 220 / 2;
        int j = this.left + this.width / 2 + 220 / 2;
        int k = p_148124_2_ - this.top - this.field_77242_t + (int) this.amountScrolled - 4;
        int l = k / this.slotHeight;
        return p_148124_1_ < this.getScrollBarX() && p_148124_1_ >= i && p_148124_1_ <= j && l >= 0 && k >= 0 && l < 88888 ? l : -1;
    }

    @Override
    public int getScrollBarX() {
        return this.width / 2 + 124;
    }

    @Override
    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                int i = (this.width - 220) / 2;
                int j = (this.width + 220) / 2;
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
                        int j2 = (this.width - 220) / 2;
                        int k2 = (this.width + 220) / 2;
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
}
