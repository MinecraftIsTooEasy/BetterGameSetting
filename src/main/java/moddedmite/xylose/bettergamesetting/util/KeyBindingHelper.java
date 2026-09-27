package moddedmite.xylose.bettergamesetting.util;

import moddedmite.rustedironcore.api.keybinding.KeybindingV1;
import moddedmite.xylose.bettergamesetting.api.IKeyBinding;
import net.minecraft.I18n;
import net.minecraft.KeyBinding;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class KeyBindingHelper {
    public static final KeybindingV1.Category UNCATEGORIZED = KeybindingV1.Category.register(new ResourceLocation("uncategorized"));
    private static final Map<Integer, String> KEY_NAMES = createKeyNames();

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

    private static Map<Integer, String> createKeyNames() {
        Map<Integer, String> names = new HashMap<>();
        names.put(Keyboard.KEY_NONE, "unknown");
        names.put(Keyboard.KEY_ESCAPE, "escape");
        names.put(Keyboard.KEY_MINUS, "minus");
        names.put(Keyboard.KEY_EQUALS, "equal");
        names.put(Keyboard.KEY_BACK, "backspace");
        names.put(Keyboard.KEY_TAB, "tab");
        names.put(Keyboard.KEY_LBRACKET, "left.bracket");
        names.put(Keyboard.KEY_RBRACKET, "right.bracket");
        names.put(Keyboard.KEY_RETURN, "enter");
        names.put(Keyboard.KEY_LCONTROL, "left.control");
        names.put(Keyboard.KEY_SEMICOLON, "semicolon");
        names.put(Keyboard.KEY_APOSTROPHE, "apostrophe");
        names.put(Keyboard.KEY_GRAVE, "grave.accent");
        names.put(Keyboard.KEY_LSHIFT, "left.shift");
        names.put(Keyboard.KEY_BACKSLASH, "backslash");
        names.put(Keyboard.KEY_COMMA, "comma");
        names.put(Keyboard.KEY_PERIOD, "period");
        names.put(Keyboard.KEY_SLASH, "slash");
        names.put(Keyboard.KEY_RSHIFT, "right.shift");
        names.put(Keyboard.KEY_MULTIPLY, "keypad.multiply");
        names.put(Keyboard.KEY_LMENU, "left.alt");
        names.put(Keyboard.KEY_SPACE, "space");
        names.put(Keyboard.KEY_CAPITAL, "caps.lock");
        names.put(Keyboard.KEY_NUMLOCK, "num.lock");
        names.put(Keyboard.KEY_SCROLL, "scroll.lock");
        names.put(Keyboard.KEY_SUBTRACT, "keypad.subtract");
        names.put(Keyboard.KEY_ADD, "keypad.add");
        names.put(Keyboard.KEY_DECIMAL, "keypad.decimal");
        names.put(Keyboard.KEY_NUMPAD0, "keypad.0");
        names.put(Keyboard.KEY_NUMPAD1, "keypad.1");
        names.put(Keyboard.KEY_NUMPAD2, "keypad.2");
        names.put(Keyboard.KEY_NUMPAD3, "keypad.3");
        names.put(Keyboard.KEY_NUMPAD4, "keypad.4");
        names.put(Keyboard.KEY_NUMPAD5, "keypad.5");
        names.put(Keyboard.KEY_NUMPAD6, "keypad.6");
        names.put(Keyboard.KEY_NUMPAD7, "keypad.7");
        names.put(Keyboard.KEY_NUMPAD8, "keypad.8");
        names.put(Keyboard.KEY_NUMPAD9, "keypad.9");
        names.put(Keyboard.KEY_NUMPADEQUALS, "keypad.equal");
        names.put(Keyboard.KEY_NUMPADENTER, "keypad.enter");
        names.put(Keyboard.KEY_DIVIDE, "keypad.divide");
        names.put(Keyboard.KEY_SYSRQ, "print.screen");
        names.put(Keyboard.KEY_RMENU, "right.alt");
        names.put(Keyboard.KEY_PAUSE, "pause");
        names.put(Keyboard.KEY_HOME, "home");
        names.put(Keyboard.KEY_UP, "up");
        names.put(Keyboard.KEY_PRIOR, "page.up");
        names.put(Keyboard.KEY_LEFT, "left");
        names.put(Keyboard.KEY_RIGHT, "right");
        names.put(Keyboard.KEY_END, "end");
        names.put(Keyboard.KEY_DOWN, "down");
        names.put(Keyboard.KEY_NEXT, "page.down");
        names.put(Keyboard.KEY_INSERT, "insert");
        names.put(Keyboard.KEY_DELETE, "delete");
        names.put(Keyboard.KEY_LMETA, "left.win");
        names.put(Keyboard.KEY_RMETA, "right.win");
        names.put(Keyboard.KEY_APPS, "menu");
        return names;
    }

    public static String getKeyDisplayString(int key) {
        if (key < 0) {
            if (key == -100) return I18n.getString("key.mouse.left");
            if (key == -99) return I18n.getString("key.mouse.right");
            if (key == -98) return I18n.getString("key.mouse.middle");
            return I18n.getStringParams("key.mouseButton", key + 101);
        }
        if (key >= Keyboard.KEYBOARD_SIZE) return String.format("%c", (char) (key - Keyboard.KEYBOARD_SIZE)).toUpperCase();
        String name = KEY_NAMES.get(key);
        if (name != null) return I18n.getString("key.keyboard." + name);
        String raw = Keyboard.getKeyName(key);
        return raw != null ? raw : I18n.getString("key.keyboard.unknown");
    }
}
