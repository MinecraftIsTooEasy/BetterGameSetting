package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.List;

@Mixin(value = RenderGlobal.class, priority = 3001)
public abstract class RenderGlobalMixin {
    @Shadow
    private int renderChunksWide;
    @Shadow
    private int renderDistance;
    @Shadow
    private int renderChunksTall;
    @Shadow
    private int renderChunksDeep;

    @Shadow private WorldClient theWorld;

    @Shadow private Minecraft mc;

    @Shadow public WorldRenderer[] worldRenderers;

    @Shadow private WorldRenderer[] sortedWorldRenderers;

    @Shadow private int minBlockX;

    @Shadow private int minBlockY;

    @Shadow private int minBlockZ;

    @Shadow private int maxBlockX;

    @Shadow private int maxBlockY;

    @Shadow private int maxBlockZ;

    @Shadow private List worldRenderersToUpdate;

    @Shadow public List tileEntities;

    @Shadow private boolean occlusionEnabled;

    @Shadow protected abstract void markRenderersForNewPosition(int par1, int par2, int par3);

    @Shadow private int renderEntitiesStartupCounter;

    @Shadow private IntBuffer glOcclusionQueryBase;

    @Shadow private int glRenderListBase;

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
//    @Overwrite
//    public void loadRenderers() {
//        if (this.theWorld != null) {
//            Block.leaves.setGraphicsLevel(this.mc.gameSettings.isFancyGraphicsEnabled());
//            this.renderDistance = this.mc.gameSettings.getRenderDistance();
//            int i;
//
//            if (this.worldRenderers != null) {
//                for (i = 0; i < this.worldRenderers.length; ++i) {
//                    this.worldRenderers[i].stopRendering();
//                }
//            }
//
//            i = this.renderDistance * 2 + 1;
//            this.renderChunksWide = i;
//            this.renderChunksTall = 16;
//            this.renderChunksDeep = i;
//            this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
//            this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
//            int j = 0;
//            int k = 0;
//            this.minBlockX = 0;
//            this.minBlockY = 0;
//            this.minBlockZ = 0;
//            this.maxBlockX = this.renderChunksWide;
//            this.maxBlockY = this.renderChunksTall;
//            this.maxBlockZ = this.renderChunksDeep;
//            int l;
//
//            for (l = 0; l < this.worldRenderersToUpdate.size(); ++l) {
//                ((WorldRenderer) this.worldRenderersToUpdate.get(l)).needsUpdate = false;
//            }
//
//            this.worldRenderersToUpdate.clear();
//            this.tileEntities.clear();
////            this.onStaticEntitiesChanged();
//
//            for (l = 0; l < this.renderChunksWide; ++l) {
//                for (int i1 = 0; i1 < this.renderChunksTall; ++i1) {
//                    for (int j1 = 0; j1 < this.renderChunksDeep; ++j1) {
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l] = new WorldRenderer(this.theWorld, this.tileEntities, l * 16, i1 * 16, j1 * 16, this.glRenderListBase + j);
//
//                        if (this.occlusionEnabled) {
//                            this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].glOcclusionQuery = this.glOcclusionQueryBase.get(k);
//                        }
//
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isWaitingOnOcclusionQuery = false;
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isVisible = true;
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].isInFrustum = true;
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].chunkIndex = k++;
//                        this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l].markDirty();
//                        this.sortedWorldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l] = this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l];
//                        this.worldRenderersToUpdate.add(this.worldRenderers[(j1 * this.renderChunksTall + i1) * this.renderChunksWide + l]);
//                        j += 3;
//                    }
//                }
//            }
//
//            if (this.theWorld != null) {
//                EntityLivingBase entitylivingbase = this.mc.renderViewEntity;
//
//                if (entitylivingbase != null) {
//                    this.markRenderersForNewPosition(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ));
//                    Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entitylivingbase));
//                }
//            }
//
//            this.renderEntitiesStartupCounter = 2;
//        }
//    }
}
