package moddedmite.xylose.bettergamesetting.util;

import net.minecraft.ChunkCoordIntPair;
import net.minecraft.ChunkProviderServer;
import net.minecraft.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility for unloading chunks from memory and persisting them through the normal chunk unload flow.
 * <p>
 * This utility does not delete any region file. It only queues chunks for unload, drains unload ticks,
 * and flushes save handlers.
 */

public final class ChunkUnloadSaveUtil {
	private static final Logger LOGGER = LogManager.getLogger(ChunkUnloadSaveUtil.class);
	private static final int DEFAULT_MAX_DRAIN_PASSES = 64;
	
	private static volatile Field chunksToUnloadField;
	private static volatile boolean forceQueueUnavailable;
	
	public static ChunkUnloadResult unloadAndSaveChunk(WorldServer world, int chunkX, int chunkZ) {
		return unloadAndSaveChunk(world, chunkX, chunkZ, false, DEFAULT_MAX_DRAIN_PASSES);
	}
	
	public static ChunkUnloadResult unloadAndSaveChunk(WorldServer world, int chunkX, int chunkZ, boolean forceQueue, int maxDrainPasses) {
		return unloadAndSaveChunks(world, chunkX, chunkZ, chunkX, chunkZ, forceQueue, maxDrainPasses);
	}
	
	public static ChunkUnloadResult unloadAndSaveChunks(WorldServer world, int minChunkX, int minChunkZ, int maxChunkX, int maxChunkZ) {
		return unloadAndSaveChunks(world, minChunkX, minChunkZ, maxChunkX, maxChunkZ, false, DEFAULT_MAX_DRAIN_PASSES);
	}
	
	public static ChunkUnloadResult unloadAndSaveChunks(
			WorldServer world,
			int minChunkX,
			int minChunkZ,
			int maxChunkX,
			int maxChunkZ,
			boolean forceQueue,
			int maxDrainPasses
	) {
		if (world == null) {
			throw new IllegalArgumentException("world is null");
		}
		if (maxDrainPasses <= 0) {
			throw new IllegalArgumentException("maxDrainPasses must be > 0");
		}
		
		int startX = Math.min(minChunkX, maxChunkX);
		int endX = Math.max(minChunkX, maxChunkX);
		int startZ = Math.min(minChunkZ, maxChunkZ);
		int endZ = Math.max(minChunkZ, maxChunkZ);
		
		ChunkProviderServer provider = world.theChunkProviderServer;
		if (provider == null) {
			throw new IllegalStateException("world.theChunkProviderServer is null");
		}
		
		List<ChunkCoordIntPair> loadedTargets = collectLoadedTargets(provider, startX, startZ, endX, endZ);
		int loadedBefore = loadedTargets.size();
		int queued = 0;
		boolean forceFallbackUsed = false;
		
		for (ChunkCoordIntPair chunkPos : loadedTargets) {
			if (forceQueue) {
				if (tryForceQueueChunk(provider, chunkPos.chunkXPos, chunkPos.chunkZPos)) {
					queued++;
				} else {
					provider.unloadChunksIfNotNearSpawn(chunkPos.chunkXPos, chunkPos.chunkZPos);
					queued++;
					forceFallbackUsed = true;
				}
			} else {
				provider.unloadChunksIfNotNearSpawn(chunkPos.chunkXPos, chunkPos.chunkZPos);
				queued++;
			}
		}
		
		int passesUsed = 0;
		int loadedAfter = loadedBefore;
		
		while (loadedAfter > 0 && passesUsed < maxDrainPasses) {
			passesUsed++;
			provider.unloadQueuedChunks();
			loadedAfter = countLoaded(provider, loadedTargets);
		}
		
		world.flush();
		
		int unloaded = loadedBefore - loadedAfter;
		boolean allUnloaded = loadedAfter == 0;
		
		if (!allUnloaded) {
			LOGGER.warn(
					"Chunk unload did not fully drain. dim={}, range=[{},{}]-[{},{}], loadedBefore={}, loadedAfter={}, passesUsed={}, forceQueue={}, forceFallbackUsed={}",
					world.provider.dimensionId,
					startX, startZ, endX, endZ,
					loadedBefore, loadedAfter, passesUsed, forceQueue, forceFallbackUsed
			);
		}
		
		return new ChunkUnloadResult(
				world.provider.dimensionId,
				startX,
				startZ,
				endX,
				endZ,
				loadedBefore,
				queued,
				unloaded,
				loadedAfter,
				passesUsed,
				allUnloaded,
				forceQueue,
				forceFallbackUsed
		);
	}
	
	private static List<ChunkCoordIntPair> collectLoadedTargets(ChunkProviderServer provider, int startX, int startZ, int endX, int endZ) {
		List<ChunkCoordIntPair> result = new ArrayList<>();
		for (int chunkX = startX; chunkX <= endX; chunkX++) {
			for (int chunkZ = startZ; chunkZ <= endZ; chunkZ++) {
				if (provider.chunkExists(chunkX, chunkZ)) {
					result.add(new ChunkCoordIntPair(chunkX, chunkZ));
				}
			}
		}
		return result;
	}
	
	private static int countLoaded(ChunkProviderServer provider, List<ChunkCoordIntPair> targets) {
		int loaded = 0;
		for (ChunkCoordIntPair chunkPos : targets) {
			if (provider.chunkExists(chunkPos.chunkXPos, chunkPos.chunkZPos)) {
				loaded++;
			}
		}
		return loaded;
	}
	
	private static boolean tryForceQueueChunk(ChunkProviderServer provider, int chunkX, int chunkZ) {
		if (forceQueueUnavailable) {
			return false;
		}
		try {
			Field field = chunksToUnloadField;
			if (field == null) {
				field = ChunkProviderServer.class.getDeclaredField("chunksToUnload");
				field.setAccessible(true);
				chunksToUnloadField = field;
			}
			@SuppressWarnings("unchecked")
			Set<Long> chunksToUnload = (Set<Long>) field.get(provider);
			chunksToUnload.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
			return true;
		} catch (Throwable t) {
			forceQueueUnavailable = true;
			LOGGER.warn("Force queue unavailable, fallback to unloadChunksIfNotNearSpawn. reason={}", t.toString());
			return false;
		}
	}
	
	public static final class ChunkUnloadResult {
		public final int dimensionId;
		public final int minChunkX;
		public final int minChunkZ;
		public final int maxChunkX;
		public final int maxChunkZ;
		public final int loadedBefore;
		public final int queued;
		public final int unloaded;
		public final int loadedAfter;
		public final int passesUsed;
		public final boolean allUnloaded;
		public final boolean forceQueueRequested;
		public final boolean forceQueueFallbackUsed;
		
		private ChunkUnloadResult(
				int dimensionId,
				int minChunkX,
				int minChunkZ,
				int maxChunkX,
				int maxChunkZ,
				int loadedBefore,
				int queued,
				int unloaded,
				int loadedAfter,
				int passesUsed,
				boolean allUnloaded,
				boolean forceQueueRequested,
				boolean forceQueueFallbackUsed
		) {
			this.dimensionId = dimensionId;
			this.minChunkX = minChunkX;
			this.minChunkZ = minChunkZ;
			this.maxChunkX = maxChunkX;
			this.maxChunkZ = maxChunkZ;
			this.loadedBefore = loadedBefore;
			this.queued = queued;
			this.unloaded = unloaded;
			this.loadedAfter = loadedAfter;
			this.passesUsed = passesUsed;
			this.allUnloaded = allUnloaded;
			this.forceQueueRequested = forceQueueRequested;
			this.forceQueueFallbackUsed = forceQueueFallbackUsed;
		}
	}
}
