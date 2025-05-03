package moddedmite.xylose.bettergamesetting.mixin.common.client.resources;

import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import moddedmite.xylose.bettergamesetting.api.IResourcePackRepository;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Iterator;
import java.util.List;

@Mixin(ResourcePackRepository.class)
public class ResourcePackRepositoryMixin implements IResourcePackRepository {
    @Shadow private List repositoryEntries;
    @Shadow private List repositoryEntriesAll;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadResourcePacks(File resourcePack, ResourcePack metadataSerializer, MetadataSerializer gameSettings, GameSettings par4, CallbackInfo ci) {

        for (String s : ((IGameSetting) par4).getResourcePacks()) {
            for (Object o : this.repositoryEntriesAll) {
                ResourcePackRepositoryEntry entry = (ResourcePackRepositoryEntry) o;
                if (entry.getResourcePackName().equals(s)) {
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
