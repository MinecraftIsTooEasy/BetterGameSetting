package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
//    @Shadow
//    public boolean needsUpdate;
//
//    @Shadow private int posX;
//
//    @Shadow private int posY;
//
//    @Shadow private int posZ;
//
//    @Shadow public boolean[] skipRenderPass;
//
//    @Shadow public List tileEntityRenderers;
//
//    @Shadow public World worldObj;
//
//    @Shadow public static int chunksUpdated;
//
//    @Shadow private int bytesDrawn;
//
//    @Shadow private List tileEntities;
//
//    @Shadow public boolean isChunkLit;
//
//    @Shadow public boolean isInitialized;
//
//    @Shadow private int glRenderList;
//
//    @Shadow protected abstract void setupGLTranslation();
//
//    @Overwrite
//    public void updateRenderer() {
//        if (this.needsUpdate) {
//            this.needsUpdate = false;
//            int i = this.posX;
//            int j = this.posY;
//            int k = this.posZ;
//            int l = this.posX + 16;
//            int i1 = this.posY + 16;
//            int j1 = this.posZ + 16;
//
//            for (int k1 = 0; k1 < 2; ++k1) {
//                this.skipRenderPass[k1] = true;
//            }
//
//            Chunk.isLit = false;
//            HashSet hashset = new HashSet();
//            hashset.addAll(this.tileEntityRenderers);
//            this.tileEntityRenderers.clear();
//            Minecraft minecraft = Minecraft.getMinecraft();
//            EntityLivingBase entitylivingbase1 = minecraft.renderViewEntity;
//            int l1 = MathHelper.floor_double(entitylivingbase1.posX);
//            int i2 = MathHelper.floor_double(entitylivingbase1.posY);
//            int j2 = MathHelper.floor_double(entitylivingbase1.posZ);
//            byte b0 = 1;
//            ChunkCache chunkcache = new ChunkCache(this.worldObj, i - b0, j - b0, k - b0, l + b0, i1 + b0, j1 + b0, b0);
//
//            if (!chunkcache.extendedLevelsInChunkCache()) {
//                ++chunksUpdated;
//                RenderBlocks renderblocks = new RenderBlocks(chunkcache);
//                this.bytesDrawn = 0;
////                this.vertexState = null;
//
//                for (int k2 = 0; k2 < 2; ++k2) {
//                    boolean flag = false;
//                    boolean flag1 = false;
//                    boolean flag2 = false;
//
//                    for (int l2 = j; l2 < i1; ++l2) {
//                        for (int i3 = k; i3 < j1; ++i3) {
//                            for (int j3 = i; j3 < l; ++j3) {
//                                Block block = chunkcache.getBlock(j3, l2, i3);
//
//                                if (block != null && block.blockMaterial != Material.air) {
//                                    if (!flag2) {
//                                        flag2 = true;
//                                        this.preRenderBlocks(k2);
//                                    }
//
//                                    if (k2 == 0 && block.hasTileEntity()) {
//                                        TileEntity tileentity = chunkcache.getBlockTileEntity(j3, l2, i3);
//
////                                        if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(tileentity)) {
////                                            this.tileEntityRenderers.add(tileentity);
////                                        }
//                                    }
//
//                                    int k3 = block.getRenderBlockPass();
//
//                                    if (k3 > k2) {
//                                        flag = true;
//                                    }
//
//                                    if (!(block.getRenderBlockPass() == k2)) continue;
//
//                                    {
//                                        flag1 |= renderblocks.renderBlockByRenderType(block, j3, l2, i3);
//
////                                        if (block.getRenderType() == 0 && j3 == l1 && l2 == i2 && i3 == j2) {
////                                            renderblocks.setRenderFromInside(true);
////                                            renderblocks.setRenderAllFaces(true);
////                                            renderblocks.renderBlockByRenderType(block, j3, l2, i3);
////                                            renderblocks.setRenderFromInside(false);
////                                            renderblocks.setRenderAllFaces(false);
////                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    if (flag1) {
//                        this.skipRenderPass[k2] = false;
//                    }
//
//                    if (flag2) {
//                        this.postRenderBlocks(k2, entitylivingbase1);
//                    } else {
//                        flag1 = false;
//                    }
//
//                    if (!flag) {
//                        break;
//                    }
//                }
//            }
//
//            HashSet hashset1 = new HashSet();
//            hashset1.addAll(this.tileEntityRenderers);
//            hashset1.removeAll(hashset);
//            this.tileEntityRenderers.addAll(hashset1);
//            hashset.removeAll(this.tileEntityRenderers);
//            this.tileEntities.removeAll(hashset);
//            this.isChunkLit = Chunk.isLit;
//            this.isInitialized = true;
//        }
//    }
//
//    private void preRenderBlocks(int p_147890_1_) {
//        GL11.glNewList(this.glRenderList + p_147890_1_, GL11.GL_COMPILE);
//        GL11.glPushMatrix();
//        this.setupGLTranslation();
//        float f = 1.000001F;
//        GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
//        GL11.glScalef(f, f, f);
//        GL11.glTranslatef(8.0F, 8.0F, 8.0F);
//        Tessellator.instance.startDrawingQuads();
//        Tessellator.instance.setTranslation((double) (-this.posX), (double) (-this.posY), (double) (-this.posZ));
//    }
//
//    private void postRenderBlocks(int p_147891_1_, EntityLivingBase p_147891_2_) {
////        if (p_147891_1_ == 1 && !this.skipRenderPass[p_147891_1_]) {
////            this.vertexState = Tessellator.instance.getVertexState((float) p_147891_2_.posX, (float) p_147891_2_.posY, (float) p_147891_2_.posZ);
////        }
//
//        this.bytesDrawn += Tessellator.instance.draw();
//        GL11.glPopMatrix();
//        GL11.glEndList();
//        Tessellator.instance.setTranslation(0.0D, 0.0D, 0.0D);
//    }
//
////    public void updateRendererSort(EntityLivingBase p_147889_1_) {
////        if (this.vertexState != null && !this.skipRenderPass[1]) {
////            this.preRenderBlocks(1);
////            Tessellator.instance.setVertexState(this.vertexState);
////            this.postRenderBlocks(1, p_147889_1_);
////        }
////    }
}
