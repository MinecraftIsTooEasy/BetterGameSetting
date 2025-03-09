package moddedmite.xylose.bettergamesetting.mixin.common.client.gui;

import moddedmite.xylose.bettergamesetting.util.GuiScreenTemporaryResourcePackSelectHelper;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(GuiScreenTemporaryResourcePackSelectSelectionList.class)
public abstract class GuiScreenTemporaryResourcePackSelectSelectionListMixin extends GuiSlot {
    @Mutable @Final @Shadow private final ResourcePackRepository field_110511_b;
    @Mutable @Final @Shadow final GuiScreenTemporaryResourcePackSelect field_110512_a;

    public GuiScreenTemporaryResourcePackSelectSelectionListMixin(GuiScreenTemporaryResourcePackSelect par1GuiScreenTemporaryResourcePackSelect, ResourcePackRepository par2ResourcePackRepository) {
        super(GuiScreenTemporaryResourcePackSelectHelper.func_110344_a(par1GuiScreenTemporaryResourcePackSelect), par1GuiScreenTemporaryResourcePackSelect.width, par1GuiScreenTemporaryResourcePackSelect.height, 32, par1GuiScreenTemporaryResourcePackSelect.height - 55 + 4, 36);
        this.field_110512_a = par1GuiScreenTemporaryResourcePackSelect;
        this.field_110511_b = par2ResourcePackRepository;
        par2ResourcePackRepository.updateRepositoryEntriesAll();
    }

    /**
     * @author Xy_Lose
     * @reason able clicked resource packs
     */
    @Overwrite
    protected void elementClicked(int par1, boolean par2) {
        List var3 = this.field_110511_b.getRepositoryEntriesAll();

        try {
            if (par1 == 0) {
                throw new RuntimeException("This is so horrible ;D");
            }

            this.field_110511_b.setRepositoryEntries((ResourcePackRepositoryEntry) var3.get(par1 - 1));
            GuiScreenTemporaryResourcePackSelectHelper.func_110341_b(this.field_110512_a).refreshResources();
        } catch (Exception var5) {
            this.field_110511_b.setRepositoryEntries();
            GuiScreenTemporaryResourcePackSelectHelper.func_110339_c(this.field_110512_a).refreshResources();
        }

        GuiScreenTemporaryResourcePackSelectHelper.func_110345_d(this.field_110512_a).gameSettings.skin = this.field_110511_b.getResourcePackName();
        GuiScreenTemporaryResourcePackSelectHelper.func_110334_e(this.field_110512_a).gameSettings.saveOptions();
    }

    /**
     * @author Xy_Lose
     * @reason able select resource packs
     */
    @Overwrite
    protected boolean isSelected(int par1) {
        List var2 = this.field_110511_b.getRepositoryEntries();
        return par1 == 0 ? var2.isEmpty() : var2.contains(this.field_110511_b.getRepositoryEntriesAll().get(par1 - 1));
    }
}
