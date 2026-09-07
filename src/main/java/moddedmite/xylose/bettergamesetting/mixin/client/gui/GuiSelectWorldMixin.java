package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelection;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiSelectWorld;
import net.minecraft.Minecraft;
import net.minecraft.SaveFormatComparator;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiSelectWorld.class)
public abstract class GuiSelectWorldMixin extends GuiScreen implements IGuiSelectWorld {
    @Shadow protected String screenTitle;
    @Shadow private int selectedWorld;
    @Shadow private List saveList;
    @Shadow private GuiButton buttonDelete;
    @Shadow private GuiButton buttonSelect;
    @Shadow private GuiButton buttonRename;
    @Shadow private GuiButton buttonRecreate;

    @Unique private GuiListWorldSelection worldSelectionList;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void bgs$createWorldSelectionList(CallbackInfo ci) {
        if (this.saveList == null) return;
        this.worldSelectionList = new GuiListWorldSelection(ReflectHelper.dyCast(this), this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.worldSelectionList.registerScrollButtons(4, 5);
    }

    /**
     * @author Xy_Lose
     * @reason
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.clearWorldSessionClientData();
        if (this.worldSelectionList != null) {
            this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);
        }
        this.fontRenderer.drawStringWithShadow(this.screenTitle, this.width / 2 - this.fontRenderer.getStringWidth(this.screenTitle) / 2, 20, 16777215);
        for (Object o : this.buttonList) {
            ((GuiButton) o).drawButton(this.mc, mouseX, mouseY);
        }
    }

    @Override
    public List<SaveFormatComparator> getSaveList() {
        return this.saveList;
    }

    @Override
    public int getSelectedIndex() {
        return this.selectedWorld;
    }

    @Override
    public void onElementClicked(int index, boolean isDoubleClick) {
        this.selectedWorld = index;
        boolean inRange = index >= 0 && index < this.saveList.size();
        boolean passed = inRange && this.selectedWorldPassedValidation();
        this.buttonSelect.enabled = passed;
        this.buttonRename.enabled = inRange;
        this.buttonDelete.enabled = inRange;
        this.buttonRecreate.enabled = passed;
        if (isDoubleClick && inRange) {
            this.selectWorld(index);
        }
    }

    @Shadow
    public abstract boolean selectedWorldPassedValidation();

    @Shadow
    public abstract void selectWorld(int par1);
}
