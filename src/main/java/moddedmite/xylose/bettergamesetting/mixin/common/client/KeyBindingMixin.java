package moddedmite.xylose.bettergamesetting.mixin.common.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import net.minecraft.KeyBinding;
import net.minecraft.I18n;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding {
    @Shadow public String keyDescription;
    @Shadow public int keyCode;
    @Shadow public static List keybindArray;
    @Unique private final String keyCategory;

    public KeyBindingMixin(String keyCategory) {
        this.keyCategory = keyCategory;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String keyDescription, int keyCode, CallbackInfo ci) {
//        KeyBindingExtra.defaultKeyCode = keyCode;
    }

    @Override
    public String getKeyCategory() {
        return this.keyCategory;
    }

    @Override
    public int compareTo(KeyBinding p_compareTo_1_) {
        int i = I18n.getString(this.keyCategory).compareTo(I18n.getString(this.keyCategory));

        if (i == 0) {
            i = I18n.getString(this.keyDescription).compareTo(I18n.getString(p_compareTo_1_.keyDescription));
        }

        return i;
    }

    @Override
    public int getKeyCode() {
        return this.keyCode;
    }

    @Override
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public int getDefaultKeyCode(String keyDescription, int keyCode) {
        return KeyBindingExtra.getKeyCodeDefault(keyDescription);
    }
}