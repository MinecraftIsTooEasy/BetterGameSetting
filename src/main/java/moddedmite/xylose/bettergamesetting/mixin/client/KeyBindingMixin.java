package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import net.minecraft.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = KeyBinding.class, priority = 999)
public class KeyBindingMixin implements IKeyBinding {
    @Shadow public static List keybindArray;

    @Unique private int defaultKey;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String keyDescription, int keyCode, CallbackInfo ci) {
        this.defaultKey = keyCode;
    }
    
    @Inject(method = "onTick", at = @At("HEAD"), cancellable = true)
    private static void onTick(int keyCode, CallbackInfo ci) {
        ci.cancel();
        if (keyCode == 0) return;
        for (KeyBinding keyBinding : (List<KeyBinding>) keybindArray) {
            if (keyBinding.keyCode == keyCode) {
                ++keyBinding.pressTime;
            }
        }
    }

    @Inject(method = "setKeyBindState", at = @At("HEAD"), cancellable = true)
    private static void setKeyBindState(int keyCode, boolean pressed, CallbackInfo ci) {
        ci.cancel();
        if (keyCode == 0) return;
        for (KeyBinding keyBinding : (List<KeyBinding>) keybindArray) {
            if (keyBinding.keyCode == keyCode) {
                keyBinding.pressed = pressed;
            }
        }
    }

    @Override
    public int getDefaultKey() {
        return this.defaultKey;
    }
}
