package moddedmite.xylose.bettergamesetting.util;

import net.minecraft.Chunk;
import net.minecraft.ChunkPosition;
import net.minecraft.TileEntity;
import net.minecraft.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ChunkTileEntities {
	private ChunkTileEntities() {
	}

	public static List<TileEntity> inBox(WorldServer worldServer, int par1, int par2, int par3, int par4, int par5, int par6) {
		List<TileEntity> result = new ArrayList<>();
		Chunk chunk = worldServer.theChunkProviderServer.getChunkIfItExists(par1 >> 4, par3 >> 4);
		if (chunk == null) return result;
		Map<ChunkPosition, TileEntity> tileEntityMap = chunk.chunkTileEntityMap;
		for (TileEntity tileEntity : tileEntityMap.values()) {
			if (tileEntity.yCoord < par2 || tileEntity.yCoord >= par5) continue;
			if (tileEntity.xCoord < par1 || tileEntity.xCoord >= par4) continue;
			if (tileEntity.zCoord < par3 || tileEntity.zCoord >= par6) continue;
			result.add(tileEntity);
		}
		return result;
	}
}
