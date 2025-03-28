package moddedmite.xylose.bettergamesetting.client;

import net.minecraft.KeyBinding;

public class CustomKeys {

    public static KeyBinding keyBindInventory_1 = new KeyBinding("key.inventory_1", 2);
    public static KeyBinding keyBindInventory_2 = new KeyBinding("key.inventory_2", 3);
    public static KeyBinding keyBindInventory_3 = new KeyBinding("key.inventory_3", 4);
    public static KeyBinding keyBindInventory_4 = new KeyBinding("key.inventory_4", 5);
    public static KeyBinding keyBindInventory_5 = new KeyBinding("key.inventory_5", 6);
    public static KeyBinding keyBindInventory_6 = new KeyBinding("key.inventory_6", 7);
    public static KeyBinding keyBindInventory_7 = new KeyBinding("key.inventory_7", 8);
    public static KeyBinding keyBindInventory_8 = new KeyBinding("key.inventory_8", 9);
    public static KeyBinding keyBindInventory_9 = new KeyBinding("key.inventory_9", 10);
    public static KeyBinding keyBindPrintScreen = new KeyBinding("key.printScreen", 60);
    public static KeyBinding keyBindPersonView = new KeyBinding("key.personView", 63);

    public static int inventoryKeyProvider(int inventory_slot) {
        if (inventory_slot == 0) return keyBindInventory_1.keyCode;
        if (inventory_slot == 1) return keyBindInventory_2.keyCode;
        if (inventory_slot == 2) return keyBindInventory_3.keyCode;
        if (inventory_slot == 3) return keyBindInventory_4.keyCode;
        if (inventory_slot == 4) return keyBindInventory_5.keyCode;
        if (inventory_slot == 5) return keyBindInventory_6.keyCode;
        if (inventory_slot == 6) return keyBindInventory_7.keyCode;
        if (inventory_slot == 7) return keyBindInventory_8.keyCode;
        if (inventory_slot == 8) return keyBindInventory_9.keyCode;
        return 0;
    }

    public static int printScreenKeyProvider() {
        return keyBindPrintScreen.keyCode;
    }

    public static int personViewKeyProvider() {
        return keyBindPersonView.keyCode;
    }

    public static KeyBinding[] getMyKeybindings() {
        return new KeyBinding[]{keyBindInventory_1, keyBindInventory_2, keyBindInventory_3, keyBindInventory_4, keyBindInventory_5, keyBindInventory_6, keyBindInventory_7, keyBindInventory_8, keyBindInventory_9, keyBindPrintScreen, keyBindPersonView};
    }
}
