package moddedmite.xylose.bettergamesetting.mixin.common.client.resources;

import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import net.minecraft.PackMetadataSection;
import net.minecraft.ResourcePackRepositoryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ResourcePackRepositoryEntry.class)
public class ResourcePackRepositoryEntryMixin implements IResourcePackRepository {
    @Shadow private PackMetadataSection rePackMetadataSection;

    @Override
    public int getPackFormat() {
        return this.rePackMetadataSection.getPackFormat();
    }
}
