package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.xylose.bettergamesetting.api.IGameSetting;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {
    @Shadow private int renderChunksWide;
    @Shadow private int renderDistance;
    @Shadow private int renderChunksTall;
    @Shadow private int renderChunksDeep;
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
    @Shadow private IntBuffer glOcclusionQueryBase;
    @Shadow private int glRenderListBase;
    @Shadow protected abstract void markRenderersForNewPosition(int par1, int par2, int par3);
    @Shadow private int renderEntitiesStartupCounter;

    //    @Inject(method = "loadRenderers", at = @At(value = "HEAD"))
//    private void test(CallbackInfo ci) {
//        int var1 = Math.min(65, (this.renderDistance * 2) + 1);
//        this.renderChunksWide = var1;
//        this.renderChunksTall = 16;
//        this.renderChunksDeep = var1;
//    }
    @Overwrite
    public void loadRenderers() {
        EntityLivingBase var7;
        if (this.theWorld == null) {
            return;
        }
        Block.leaves.setGraphicsLevel(this.mc.gameSettings.isFancyGraphicsEnabled() && !Main.is_MITE_DS);
        this.renderDistance = this.mc.gameSettings.getRenderDistance();
        if (this.worldRenderers != null) {
            for (WorldRenderer worldRenderer : this.worldRenderers) {
                worldRenderer.stopRendering();
            }
        }
        int var1 = Math.min(65, (this.renderDistance * 2) + 1);
        this.renderChunksWide = var1;
        this.renderChunksTall = 16;
        this.renderChunksDeep = var1;
        this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        int var2 = 0;
        int var3 = 0;
        this.minBlockX = 0;
        this.minBlockY = 0;
        this.minBlockZ = 0;
        this.maxBlockX = this.renderChunksWide;
        this.maxBlockY = this.renderChunksTall;
        this.maxBlockZ = this.renderChunksDeep;
        for (int var4 = 0; var4 < this.worldRenderersToUpdate.size(); var4++) {
            ((WorldRenderer)this.worldRenderersToUpdate.get(var4)).needsUpdate = false;
        }
        this.worldRenderersToUpdate.clear();
        this.tileEntities.clear();
        for (int var42 = 0; var42 < this.renderChunksWide; var42++) {
            for (int var5 = 0; var5 < this.renderChunksTall; var5++) {
                for (int var6 = 0; var6 < this.renderChunksDeep; var6++) {
                    int idx = (((var6 * this.renderChunksTall) + var5) * this.renderChunksWide) + var42;
                    this.worldRenderers[idx] = new WorldRenderer(this.theWorld, this.tileEntities, var42 * 16, var5 * 16, var6 * 16, this.glRenderListBase + var2);
                    if (this.occlusionEnabled) {
                        this.worldRenderers[idx].glOcclusionQuery = this.glOcclusionQueryBase.get(var3);
                    }
                    this.worldRenderers[idx].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[idx].isVisible = true;
                    this.worldRenderers[idx].isInFrustum = true;
                    int i = var3;
                    var3++;
                    this.worldRenderers[idx].chunkIndex = i;
                    this.worldRenderers[idx].markDirty();
                    this.sortedWorldRenderers[idx] = this.worldRenderers[idx];
                    this.worldRenderersToUpdate.add(this.worldRenderers[idx]);
                    var2 += 3;
                }
            }
        }
        if (this.theWorld != null && (var7 = this.mc.renderViewEntity) != null) {
            this.markRenderersForNewPosition(MathHelper.floor_double(var7.posX), MathHelper.floor_double(var7.posY), MathHelper.floor_double(var7.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var7));
        }
        this.renderEntitiesStartupCounter = 2;
    }

    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSound(Ljava/lang/String;FFFFF)V"))
    private void blockDestroyVolume(SoundManager instance, String var9, float var7, float v, float par1Str, float par2, float par3, Operation<Void> original) {
        instance.playSound(var9, var7, v, par1Str, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par2, par3);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 16))
    public void anvilVolume_0(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 17))
    public void anvilVolume_1(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldClient;playSound(DDDLjava/lang/String;FFZ)V", ordinal = 18))
    public void anvilVolume_2(WorldClient instance, double v, double par1, double par3, String par5, float par7Str, float par8, boolean par9, Operation<Void> original) {
        instance.playSound(v, par1, par3, par5, ((IGameSetting) this.mc.gameSettings).getBlockVolume() * par7Str, par8, par9);
    }
}
