package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.client.CustomKeys;
import net.minecraft.GuiContainer;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {
    @Shadow private Slot theSlot;
    @Shadow protected abstract void handleMouseClick(Slot par1Slot, int par2, int par3, int par4);

    @Inject(method = "checkHotbarKeys", at = @At("HEAD"), cancellable = true)
    protected void checkHotbarKeys(int par1, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getMinecraft().thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
            for (int hotBar = 0; hotBar < 9; ++hotBar) {
                if (par1 == CustomKeys.inventoryKeyProvider(hotBar)) {
                    this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, hotBar, 2);
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
