package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.List;

@Mixin(value = RenderGlobal.class, priority = 3001)
public abstract class RenderGlobalMixin {
    @Shadow private int renderChunksWide;
    @Shadow private int renderDistance;
    @Shadow private int renderChunksTall;
    @Shadow private int renderChunksDeep;
    
    @Shadow
    private Minecraft mc;
    
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 32))
    private int modify(int constant) {
        return 64;
    }

    @Inject(method = "loadRenderers", at = @At(value = "FIELD", target = "Lnet/minecraft/RenderGlobal;renderChunksDeep:I", shift = At.Shift.AFTER))
    private void loadRenderers(CallbackInfo ci) {
        int var1 = Math.min(65, (this.renderDistance * 2) + 1);
        this.renderChunksWide = var1;
        this.renderChunksTall = 16;
        this.renderChunksDeep = var1;
    }
    
//    @Inject(method = "loadRenderers", at = @At("TAIL"))
//    private void onRenderDistanceChanged(CallbackInfo ci) {
//        if (this.mc.theWorld == null || this.mc.thePlayer == null) return;
//        int newChunkRadius = this.mc.gameSettings.getRenderDistance();
//        int playerChunkX = (int)Math.floor(this.mc.thePlayer.posX) >> 4;
//        int playerChunkZ = (int)Math.floor(this.mc.thePlayer.posZ) >> 4;
//        unloadDistantChunks(this.mc.theWorld, playerChunkX, playerChunkZ, newChunkRadius);
//    }
//
//    @Unique
//    private void unloadDistantChunks(WorldClient world, int playerChunkX, int playerChunkZ, int chunkRadius) {
//        ChunkProviderClient chunkProvider = world.clientChunkProvider;
//        if (chunkProvider == null) return;
//        int scanRadius = 32;
//        for (int dx = -scanRadius; dx <= scanRadius; dx++) {
//            for (int dz = -scanRadius; dz <= scanRadius; dz++) {
//                int chunkX = playerChunkX + dx;
//                int chunkZ = playerChunkZ + dz;
//                int distance = Math.max(Math.abs(dx), Math.abs(dz));
//                if (distance > chunkRadius) {
//                    long key = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
////                    if (chunkProvider.chunkMapping.containsItem(key)) {
//                    world.doPreChunk(chunkX, chunkZ, false);
//                        chunkProvider.unloadChunk(chunkX, chunkZ);
//                    AxisAlignedBB.getAABBPool().clearPool();
//                    world.getWorldVec3Pool().clearAndFreeCache();
////                        System.gc();
////                    }
//                }
//            }
//        }
//    }
}
