package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import moddedmite.xylose.bettergamesetting.client.KeyBindingExtra;
import net.minecraft.KeyBinding;
import net.minecraft.I18n;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements IKeyBinding, Comparable<KeyBinding> {
    @Shadow public String keyDescription;
    @Shadow public int keyCode;

    @Unique private static final Map<String, Integer> DEFAULT_KEYCODES = new HashMap<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String keyDescription, int keyCode, CallbackInfo ci) {
        if (keyDescription != null && !keyDescription.isEmpty()) {
            String keyDescription1 = keyDescription;

            if (keyDescription.contains(":")) {
                String[] parts = keyDescription.split(":", 2);
                keyDescription1 = parts[1];
                KeyBindingExtra.getKeyCategoriesMap().put(keyDescription1, parts[0]);
                this.keyDescription = keyDescription1;
            }

            DEFAULT_KEYCODES.put(keyDescription1, keyCode);
        }
    }

    @Override
    public int getDefaultKeyCode(String keyDescription) {
        if (keyDescription == null || keyDescription.isEmpty()) {
            return Keyboard.KEY_NONE;
        }
        Integer value = DEFAULT_KEYCODES.get(keyDescription);
        return value != null ? value : Keyboard.KEY_NONE;
    }

    @Override
    public int compareTo(KeyBinding key) {
        String category0 = KeyBindingExtra.getKeyCategory(this.keyDescription);
        String category1 = KeyBindingExtra.getKeyCategory(key.keyDescription);

        int compare = I18n.getString(category0).compareTo(I18n.getString(category1));
        if (compare != 0) {
            return compare;
        }

        return I18n.getString(this.keyDescription).compareTo(I18n.getString(key.keyDescription));
    }

    @Override
    public int getKeyCode() {
        return this.keyCode;
    }

    @Override
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}