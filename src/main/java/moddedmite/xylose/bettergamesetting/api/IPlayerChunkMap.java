package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.PlayerInstance;

public interface IPlayerChunkMap {
	void queueChunkLoad(PlayerInstance instance);

	void removeChunkWatcher(PlayerInstance instance, int x, int z, boolean hasPendingUpdates);
}
