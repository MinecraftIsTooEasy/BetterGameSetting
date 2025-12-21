//package moddedmite.xylose.bettergamesetting.client;
//
//import net.minecraft.I18n;
//import net.minecraft.KeyBinding;
//
//public class KeyBindingExtra extends KeyBinding {
//    private final String keyCategory;
//    private int keyCategoryCount = 0;
//
//    public KeyBindingExtra(String keyCategory, String description, int keyCode) {
//        super(description, keyCode);
//        this.keyCategory = keyCategory;
//        this.keyCategoryCount++;
//    }
//
//    public String getKeyCategory(String keyDescription) {
//        if (this.keyCategory != null) {
//            return this.keyCategory;
//        }
//        return switch (keyDescription) {
//            case "key.forward", "key.jump", "key.right", "key.back", "key.left", "key.sneak", "key.toggleRun" ->
//                    I18n.getString("key.categories.movement");
//            case "key.inventory", "key.inventory_1", "key.inventory_2", "key.inventory_3", "key.inventory_4", "key.inventory_5", "key.inventory_6", "key.inventory_7", "key.inventory_8", "key.inventory_9" ->
//                    I18n.getString("key.categories.inventory");
//            case "key.drop", "key.attack", "key.use", "key.zoom", "key.pickItem" ->
//                    I18n.getString("key.categories.gameplay");
//            case "key.chat", "key.command", "key.playerlist" -> I18n.getString("key.categories.multiplayer");
//            case "key.redrawChunks", "key.printScreen", "key.personView" -> I18n.getString("key.categories.misc");
//            default -> I18n.getString("key.categories.uncategorized");
//        };
//    }
//
//    public int getKeyCategoryCount() {
//        return keyCategoryCount;
//    }
//}
