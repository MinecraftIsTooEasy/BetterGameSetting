package moddedmite.xylose.bettergamesetting.mixin.common.client.resources;

import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import net.minecraft.ResourcePackRepository;
import net.minecraft.ResourcePackRepositoryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ResourcePackRepository.class)
public class ResourcePackRepositoryMixin implements IResourcePackRepository {
    @Shadow private List repositoryEntries;

    public void func_148527_a(List<ResourcePackRepositoryEntry> p_148527_1_) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }
}
