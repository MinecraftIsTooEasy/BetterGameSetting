package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelection;
import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelectionEntry;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
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

import java.util.Arrays;
import java.util.List;

@Mixin(value = GuiSelectWorld.class, priority = 1001)
public abstract class GuiSelectWorldMixin extends GuiScreen implements IGuiSelectWorld {
    @Shadow protected String screenTitle;
    @Shadow protected GuiScreen parentScreen;
    @Shadow private List saveList;
    @Shadow private int selectedWorld;
    @Shadow private GuiButton buttonDelete;
    @Shadow private GuiButton buttonSelect;
    @Shadow private GuiButton buttonRename;
    @Shadow private GuiButton buttonRecreate;
    @Shadow public abstract void selectWorld(int par1);

    @Unique private GuiListWorldSelection worldSelectionList;
    @Unique private String worldVersTooltip;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void createWorldSelectionList(CallbackInfo ci) {
        this.worldSelectionList = new GuiListWorldSelection(ReflectHelper.dyCast(this), this.mc, this.width, this.height, 32, this.height - 64, 36);
    }

    /**
     * @author Xy_Lose
     * @reason
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Minecraft.clearWorldSessionClientData();
        this.worldVersTooltip = null;
        if (this.worldSelectionList != null) {
            this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);
        }
        this.fontRenderer.drawStringWithShadow(this.screenTitle, this.width / 2 - this.fontRenderer.getStringWidth(this.screenTitle) / 2, 20, 16777215);
        for (Object o : this.buttonList) {
            ((GuiButton) o).drawButton(this.mc, mouseX, mouseY);
        }
        if (this.worldVersTooltip != null) {
            ScreenUtil.getInstance().drawTooltip(Arrays.asList(this.worldVersTooltip.split("\n")), mouseX, mouseY);
        }
    }

    /**
     * @author Xy_Lose
     * @reason
     */
    @Overwrite
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) return;
        GuiListWorldSelectionEntry entry = this.worldSelectionList == null ? null : this.worldSelectionList.getSelectedWorld();

        if (button.id == 2) {
            if (entry != null) {
                entry.deleteWorld();
            }
        } else if (button.id == 1) {
            if (entry != null) {
                entry.joinWorld();
            }
        } else if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiCreateWorld(this));
        } else if (button.id == 4) {
            if (entry != null) {
                entry.editWorld();
            }
        } else if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 5 && entry != null) {
            entry.recreateWorld();
        }
    }

    @Override
    public List<SaveFormatComparator> getSaveList() {
        return this.saveList;
    }

    @Override
    public void selectWorld(GuiListWorldSelectionEntry entry) {
        boolean flag = entry != null;
        boolean valid = flag && entry.isPassedValidation();
        this.buttonSelect.enabled = valid;
        this.buttonDelete.enabled = flag;
        this.buttonRename.enabled = flag;
        this.buttonRecreate.enabled = valid;
    }

    @Override
    public void setVersionTooltip(String text) {
        this.worldVersTooltip = text;
    }

    @Override
    public void loadWorld(String fileName) {
        if (this.saveList == null) return;

        for (int i = 0; i < this.saveList.size(); ++i) {
            if (((SaveFormatComparator) this.saveList.get(i)).getFileName().equals(fileName)) {
                this.selectedWorld = i;
                this.selectWorld(i);
                return;
            }
        }
    }
}
