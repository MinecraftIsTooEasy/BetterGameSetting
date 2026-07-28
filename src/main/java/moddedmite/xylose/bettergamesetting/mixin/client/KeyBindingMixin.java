package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import net.minecraft.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding {
    @Unique
    private int defaultKey;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String keyDescription, int keyCode, CallbackInfo ci) {
        this.defaultKey = keyCode;
    }

    @Override
    public int getDefaultKey() {
        return this.defaultKey;
    }
}
