package moddedmite.xylose.bettergamesetting.mixin.client.audio;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(SoundsMITE.class)
public class SoundsMITEMixin {
    @Shadow private List<String> sounds = new ArrayList<String>();

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;setErrorMessage(Ljava/lang/String;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void disMITEResourcePack(CallbackInfoReturnable<Boolean> cir, boolean errors, ResourcePack MITE_resource_pack) {
        errors = false;
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void disMITEResourcePack(CallbackInfoReturnable<Boolean> cir) {
        ResourcePack MITE_resource_pack = Minecraft.MITE_resource_pack;
        if (MITE_resource_pack == null) {
            Iterator iterator = Minecraft.getMinecraft().mcResourcePackRepository.getRepositoryEntries().iterator();
            ResourcePackRepositoryEntry resourcePackRepositoryEntry = null;
            while (iterator.hasNext()) {
                resourcePackRepositoryEntry = (ResourcePackRepositoryEntry) iterator.next();
            }
            for (String sound : this.sounds) {
                if (resourcePackRepositoryEntry != null && resourcePackRepositoryEntry.getResourcePack().resourceExists(new ResourceLocation(sound))) {
                    this.loadMITESound(sound);
                    continue;
                }
            }
        }
    }

    @ModifyConstant(method = "load", constant = @Constant(stringValue = "\nSoundsMITE: MITE Resource Pack 1.6.4 needs to be loaded!"))
    private String disMITEResourcePack(String constant) {
        return null;
    }

    @Shadow
    private void loadMITESound(String path) {}
}
