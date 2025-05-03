package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.ResourcePackRepositoryEntry;

import java.util.List;

public interface IResourcePackRepository {
    default void setRepositories(List<ResourcePackRepositoryEntry> repositories) {}

    default int getPackFormat() {
        return 0;
    }
}
