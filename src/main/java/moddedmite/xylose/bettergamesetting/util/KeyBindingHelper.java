package moddedmite.xylose.bettergamesetting.util;

import moddedmite.rustedironcore.api.keybinding.KeybindingV1;
import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import net.minecraft.KeyBinding;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;

public class KeyBindingHelper {
    public static final KeybindingV1.Category UNCATEGORIZED = KeybindingV1.Category.register(new ResourceLocation("uncategorized"));

    public static KeyBinding[] allKeyBindings(Minecraft client) {
        return client.gameSettings.keyBindings;
    }

    public static KeybindingV1.Category getCategory(KeyBinding x) {
        if (x instanceof KeybindingV1 keybindingV1) return keybindingV1.getCategory();
        return UNCATEGORIZED;
    }

    public static int getDefaultKey(KeyBinding keyBinding) {
        if (keyBinding instanceof KeybindingV1 keybindingV1) return keybindingV1.getDefaultKey();
        return ((IKeyBinding) keyBinding).getDefaultKey();
    }

    public static int compare(KeyBinding k1, KeyBinding k2) {
        KeybindingV1.Category c1 = getCategory(k1);
        KeybindingV1.Category c2 = getCategory(k2);
        if (c1 != c2) {
            return Integer.compare(KeybindingV1.Category.SORT_ORDER.indexOf(c1), KeybindingV1.Category.SORT_ORDER.indexOf(c2));
        }
        return k1.keyDescription.compareTo(k2.keyDescription);
    }
}
