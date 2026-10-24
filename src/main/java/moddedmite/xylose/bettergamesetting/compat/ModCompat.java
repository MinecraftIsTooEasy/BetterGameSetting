package moddedmite.xylose.bettergamesetting.compat;

import net.minecraft.DedicatedServer;
import net.minecraft.server.MinecraftServer;
import net.xiaoyu233.fml.FishModLoader;

public final class ModCompat {

    public static final String SERVER_SLOW_CHUNK_LOADING_PROPERTY = "bgs-slow-chunk-loading";
    public static final String PLAYER_INSTANCE_SCHEDULER_MIXIN = "moddedmite.xylose.bettergamesetting.mixin.common.scheduler.PlayerInstanceMixin";

    public static final boolean HAS_PERFORMANCE_OPTIMIZATION = FishModLoader.hasMod("performance_optimization");

    public static boolean isServerSlowChunkLoadingEnabled(MinecraftServer server) {
        return !server.isDedicatedServer() || ((DedicatedServer) server).getBooleanProperty(SERVER_SLOW_CHUNK_LOADING_PROPERTY, true);
    }
}
