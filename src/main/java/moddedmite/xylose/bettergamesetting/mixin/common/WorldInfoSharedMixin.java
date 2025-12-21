package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.GameRules;
import net.minecraft.WorldInfoShared;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldInfoShared.class)
public class WorldInfoSharedMixin {
    @Shadow public GameRules theGameRules;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.theGameRules = BGSClient.gameRules;
    }

    @Inject(method = "<init>(Lnet/minecraft/NBTTagCompound;)V", at = @At("TAIL"))
    private void init1(CallbackInfo ci) {
        this.theGameRules = BGSClient.gameRules;
    }

    @Inject(method = "<init>(Lnet/minecraft/WorldSettings;Ljava/lang/String;)V", at = @At("TAIL"))
    private void init2(CallbackInfo ci) {
        this.theGameRules = BGSClient.gameRules;
    }
}
