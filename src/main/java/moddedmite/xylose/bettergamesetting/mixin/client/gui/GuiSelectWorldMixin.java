package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGuiSelectWorld;
import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelection;
import moddedmite.xylose.bettergamesetting.client.gui.world.GuiListWorldSelectionEntry;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiScreen;
import net.minecraft.GuiSelectWorld;
import net.minecraft.GuiTextField;
import net.minecraft.GuiWorldSlot;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.SaveFormatComparator;
import net.xiaoyu233.fml.util.ReflectHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(value = GuiSelectWorld.class, priority = 1001)
public abstract class GuiSelectWorldMixin extends GuiScreen implements IGuiSelectWorld {
    @Shadow protected GuiScreen parentScreen;
    @Shadow private List saveList;
    @Shadow private int selectedWorld;
    @Shadow private GuiButton buttonDelete;
    @Shadow private GuiButton buttonSelect;
    @Shadow private GuiButton buttonRename;
    @Shadow private GuiButton buttonRecreate;
    @Shadow public abstract void selectWorld(int par1);

    @Unique private GuiListWorldSelection worldSelectionList;
    @Unique private GuiTextField searchField;
    @Unique private String worldVersTooltip;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void createWorldSelectionList(CallbackInfo ci) {
        this.worldSelectionList = new GuiListWorldSelection(ReflectHelper.dyCast(this), this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.searchField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 14, 200, 15);
        this.searchField.setMaxStringLength(128);
        this.searchField.setHint(I18n.getString("options.search"));
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void drawSearchField(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.searchField.drawTextBox();
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiSelectWorld;drawCenteredString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 0), index = 3)
    private int modifyTitleY(int originalY) {
        return 4;
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    private void searchKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            this.worldSelectionList.setFilter(this.searchField.getText());
            ci.cancel();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiWorldSlot;drawScreen(IIF)V"))
    private void wrapList(GuiWorldSlot instance, int mouseX, int mouseY, float partialTicks, Operation<Void> original) {
        this.worldVersTooltip = null;
        this.worldSelectionList.drawScreen(mouseX, mouseY, partialTicks);
        if (this.worldVersTooltip != null) {
            ScreenUtil.getInstance().drawTooltip(Arrays.asList(this.worldVersTooltip.split("\n")), mouseX, mouseY);
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    protected void newAction(GuiButton button, CallbackInfo ci) {
        ci.cancel();
        if (!button.enabled) return;
        GuiListWorldSelectionEntry guiListWorldSelectionEntry = this.worldSelectionList.getSelectedWorld();
        if (button.id == 2) {
            if (guiListWorldSelectionEntry != null) {
                guiListWorldSelectionEntry.deleteWorld();
            }
        } else if (button.id == 1) {
            if (guiListWorldSelectionEntry != null) {
                guiListWorldSelectionEntry.joinWorld();
            }
        } else if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiCreateWorld(this));
        } else if (button.id == 6) {
            if (guiListWorldSelectionEntry != null) {
                guiListWorldSelectionEntry.editWorld();
            }
        } else if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 7 && guiListWorldSelectionEntry != null) {
            guiListWorldSelectionEntry.recreateWorld();
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
