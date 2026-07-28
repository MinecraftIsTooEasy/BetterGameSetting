package moddedmite.xylose.bettergamesetting.client;

import moddedmite.rustedironcore.api.event.events.KeybindingRegisterEvent;
import moddedmite.rustedironcore.api.keybinding.KeybindingV1;
import org.lwjgl.input.Keyboard;

public class CustomKeys {

    public static KeybindingV1 keyBindInventory_1 = new KeybindingV1("key.hotbar.1", Keyboard.KEY_1, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_2 = new KeybindingV1("key.hotbar.2", Keyboard.KEY_2, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_3 = new KeybindingV1("key.hotbar.3", Keyboard.KEY_3, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_4 = new KeybindingV1("key.hotbar.4", Keyboard.KEY_4, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_5 = new KeybindingV1("key.hotbar.5", Keyboard.KEY_5, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_6 = new KeybindingV1("key.hotbar.6", Keyboard.KEY_6, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_7 = new KeybindingV1("key.hotbar.7", Keyboard.KEY_7, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_8 = new KeybindingV1("key.hotbar.8", Keyboard.KEY_8, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindInventory_9 = new KeybindingV1("key.hotbar.9", Keyboard.KEY_9, KeybindingV1.Category.INVENTORY);
    public static KeybindingV1 keyBindPrintScreen = new KeybindingV1("key.screenshot", Keyboard.KEY_F2, KeybindingV1.Category.MISC);
    public static KeybindingV1 keyBindPersonView = new KeybindingV1("key.togglePerspective", Keyboard.KEY_F5, KeybindingV1.Category.MISC);

    public static int getInventoryKeyCode(int slot) {
        if (slot == 0) return keyBindInventory_1.keyCode;
        if (slot == 1) return keyBindInventory_2.keyCode;
        if (slot == 2) return keyBindInventory_3.keyCode;
        if (slot == 3) return keyBindInventory_4.keyCode;
        if (slot == 4) return keyBindInventory_5.keyCode;
        if (slot == 5) return keyBindInventory_6.keyCode;
        if (slot == 6) return keyBindInventory_7.keyCode;
        if (slot == 7) return keyBindInventory_8.keyCode;
        if (slot == 8) return keyBindInventory_9.keyCode;
        return 0;
    }

    public static int getPrintScreenKeyCode() {
        return keyBindPrintScreen.keyCode;
    }

    public static int getPersonViewKeyCode() {
        return keyBindPersonView.keyCode;
    }

    public static void registerKeybindings(KeybindingRegisterEvent event) {
        KeybindingV1[] array = {
                keyBindInventory_1,
                keyBindInventory_2,
                keyBindInventory_3,
                keyBindInventory_4,
                keyBindInventory_5,
                keyBindInventory_6,
                keyBindInventory_7,
                keyBindInventory_8,
                keyBindInventory_9,
                keyBindPrintScreen,
                keyBindPersonView
        };
        for (KeybindingV1 keyBinding : array) {
            event.register(keyBinding);
        }
    }
}
