package moddedmite.xylose.bettergamesetting.mixin.client;

import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
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

    @Unique
    private static final Map<String, Integer> DEFAULT_KEYCODES = new HashMap<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(String keyDescription, int keyCode, CallbackInfo ci) {
        if (keyDescription != null && !keyDescription.isEmpty()) {
            DEFAULT_KEYCODES.put(keyDescription, keyCode);
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
    public String getKeyCategory(String keyDescription) {
        return switch (keyDescription) {
            case "key.forward", "key.jump", "key.right", "key.back", "key.left", "key.sneak", "key.toggleRun" ->
                    I18n.getString("key.categories.movement");
            case "key.inventory", "key.inventory_1", "key.inventory_2", "key.inventory_3", "key.inventory_4",
                 "key.inventory_5", "key.inventory_6", "key.inventory_7", "key.inventory_8", "key.inventory_9" ->
                    I18n.getString("key.categories.inventory");
            case "key.drop", "key.attack", "key.use", "key.zoom", "key.pickItem" ->
                    I18n.getString("key.categories.gameplay");
            case "key.chat", "key.command", "key.playerlist" -> I18n.getString("key.categories.multiplayer");
            case "key.redrawChunks", "key.printScreen", "key.personView" -> I18n.getString("key.categories.misc");
            default -> I18n.getString("key.categories.uncategorized");
        };
    }

    @Override
    public int compareTo(KeyBinding key) {
        String category0 = this.getKeyCategory(this.keyDescription);
        String category1 = key.getKeyCategory(key.keyDescription);

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