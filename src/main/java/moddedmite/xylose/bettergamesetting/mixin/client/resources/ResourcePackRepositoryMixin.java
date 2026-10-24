package moddedmite.xylose.bettergamesetting.mixin.client.resources;

import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import net.minecraft.GameSettings;
import net.minecraft.MetadataSerializer;
import net.minecraft.ResourcePack;
import net.minecraft.ResourcePackRepository;
import net.minecraft.ResourcePackRepositoryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(ResourcePackRepository.class)
public class ResourcePackRepositoryMixin implements IResourcePackRepository {
    @Shadow private List<ResourcePackRepositoryEntry> repositoryEntries;
    @Shadow private List<ResourcePackRepositoryEntry> repositoryEntriesAll;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadResourcePacks(File resourcePackDirectory, ResourcePack defaultResourcePack, MetadataSerializer metadataSerializer, GameSettings gameSettings, CallbackInfo ci) {
        // BGS's ordered selection replaces the legacy single-pack skin option.
        // Keeping the constructor's selection would load that pack twice.
        this.repositoryEntries.clear();
        for (String selectedPack : gameSettings.getResourcePacks()) {
            // Modern options.txt identifies external packs with file/, while
            // the legacy repository exposes the raw ZIP/directory filename.
            String packName = selectedPack.startsWith("file/") ? selectedPack.substring(5) : selectedPack;
            for (ResourcePackRepositoryEntry entry : this.repositoryEntriesAll) {
                if (entry.getResourcePackName().equals(packName) && !this.repositoryEntries.contains(entry)) {
                    this.repositoryEntries.add(entry);
                    break;
                }
            }
        }
    }

    @Override
    public void setRepositories(List<ResourcePackRepositoryEntry> repositories) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(repositories);
    }
}
