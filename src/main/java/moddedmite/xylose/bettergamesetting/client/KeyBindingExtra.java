package moddedmite.xylose.bettergamesetting.client;

import net.minecraft.I18n;

import java.util.HashMap;
import java.util.Map;

public class KeyBindingExtra {
	private static final Map<String, String> KEY_CATEGORIES = new HashMap<>();
	
	public static Map<String, String> getKeyCategoriesMap() {
		return KEY_CATEGORIES;
	}
	
	public static void setKeyKeyCategory(String keyDescription, String keyCategory) {
		getKeyCategoriesMap().put(keyDescription, keyCategory);
	}
	
	public static int getKeyCategoryCount() {
		return (int) (getKeyCategoriesMap().values().stream().distinct().count() + 1);
	}
	
	public static String getKeyCategory(String keyDescription) {
		if (KeyBindingExtra.getKeyCategoriesMap().containsKey(keyDescription)) {
			return KeyBindingExtra.getKeyCategoriesMap().get(keyDescription);
		} else {
			return I18n.getString("key.categories.uncategorized");
		}
	}
}
