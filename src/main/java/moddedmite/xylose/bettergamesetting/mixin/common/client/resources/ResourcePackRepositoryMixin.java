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
        Iterator iterator = ((IGameSetting) par4).getResourcePacks().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            Iterator iterator1 = this.repositoryEntriesAll.iterator();
            while (iterator1.hasNext()) {
                ResourcePackRepositoryEntry entry = (ResourcePackRepositoryEntry) iterator1.next();
                if (entry.getResourcePackName().equals(s)) {
                    this.repositoryEntries.add(entry);
                    break;
                }
            }
        }
    }

    @Override
    public void func_148527_a(List<ResourcePackRepositoryEntry> p_148527_1_) {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }
}
