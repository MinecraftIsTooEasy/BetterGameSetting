package moddedmite.xylose.bettergamesetting.mixin.client.renderer;

import net.minecraft.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGlobal.class, priority = 3001)
public abstract class RenderGlobalMixin {
    @Shadow private int renderChunksWide;
    @Shadow private int renderDistance;
    @Shadow private int renderChunksTall;
    @Shadow private int renderChunksDeep;
    @Shadow private Minecraft mc;
    
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

    @ModifyConstant(method = "updateRenderers", constant = @Constant(intValue = 2))
    private int modifyChunkRebuildBudget(int constant) {
        return 32;
    }

    @Redirect(method = "updateRenderers", at = @At(value = "FIELD", target = "Lnet/minecraft/WorldRenderer;isInFrustum:Z", opcode = Opcodes.GETFIELD, ordinal = 1))
    private boolean ignoreFrustumWhenRebuilding(WorldRenderer instance) {
        return true;
    }

//    @WrapOperation(method = "playAuxSFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/SoundManager;playSound(Ljava/lang/String;FFFFF)V"))
//    private void replaceSoundManager(SoundManager instance, String sound, float x, float y, float z, float volume, float pitch, Operation<Void> original) {
//        this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new SoundEvent(new ResourceLocation(sound), SoundCategory.MASTER), SoundCategory.MASTER, volume, pitch, x, y, z));
//    }
}
