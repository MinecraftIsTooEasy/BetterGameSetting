package moddedmite.xylose.bettergamesetting.client.audio;

import com.google.common.collect.Maps;

import java.util.Map;

public enum SoundCategory {
    MASTER("master", 0),
    MUSIC("music", 1),
    RECORDS("record", 2),
    WEATHER("weather", 3),
    BLOCKS("block", 4),
    MOBS("hostile", 5),
    ANIMALS("neutral", 6),
    PLAYERS("player", 7),
    AMBIENT("ambient", 8),
    UI("ui", 9);

    private static final Map<String, SoundCategory> NAMES_MAP = Maps.newHashMap();
    private static final Map<Integer, SoundCategory> IDS_MAP = Maps.newHashMap();
    private final String categoryName;
    private final int categoryId;

    private SoundCategory(String name, int id) {
        this.categoryName = name;
        this.categoryId = id;
    }
    
    public String getName() {
        return this.categoryName;
    }
    
    public int getId() {
        return this.categoryId;
    }
    
    public static SoundCategory getCategory(String name) {
        return NAMES_MAP.get(name);
    }
    
    static {
	    for (SoundCategory category : values()) {
		    if (NAMES_MAP.containsKey(category.getName()) || IDS_MAP.containsKey(category.getId())) {
			    throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + category);
		    }
		    
		    NAMES_MAP.put(category.getName(), category);
		    IDS_MAP.put(category.getId(), category);
	    }
    }
}