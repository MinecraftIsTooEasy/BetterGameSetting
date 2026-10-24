package moddedmite.xylose.bettergamesetting.api;

import net.minecraft.ResourcePack;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Small compatibility bridge for client mods that ship a resource pack in
 * their jar. MITE's resource-pack repository only knows about files in the
 * resourcepacks directory, so those packs must be inserted during a reload.
 */
public final class BuiltInResourcePackRegistry {

    private static final List<ResourcePack> PACKS = new CopyOnWriteArrayList<ResourcePack>();
    private static final Map<String, ResourcePack> NAMED_PACKS = new LinkedHashMap<String, ResourcePack>();
    private static List<ResourcePack> loadedPacks = Collections.emptyList();

    /** Actual resource order, lowest priority first, including built-in packs. */
    public static List<ResourcePack> getLoadedPacks() {
        return loadedPacks;
    }

    public static void recordLoadedPacks(List<ResourcePack> packs) {
        loadedPacks = Collections.unmodifiableList(new ArrayList<ResourcePack>(packs));
    }

    public static void register(ResourcePack pack) {
        if (pack != null && !PACKS.contains(pack)) {
            PACKS.add(pack);
        }
    }

    public static void register(String id, ResourcePack pack) {
        if (id != null && pack != null) {
            NAMED_PACKS.put(id, pack);
            register(pack);
        }
    }

    /** Keep internal IDs such as {@code punchy:punchy} in options.txt. */
    public static void appendConfiguredIds(List<String> ids) {
        if (ids == null) {
            return;
        }
        for (String id : NAMED_PACKS.keySet()) {
            if (!ids.contains(id)) {
                ids.add(id);
            }
        }
    }

    /**
     * Insert built-in packs before user-selected packs. This keeps the normal
     * resource-pack precedence: a selected compatibility pack still wins over
     * a mod's default resources.
     */
    @SuppressWarnings("unchecked")
    public static void insertBeforeSelected(List packs) {
        if (packs == null || PACKS.isEmpty()) {
            return;
        }

        int insertion = packs.isEmpty() ? 0 : 1;
        for (ResourcePack pack : PACKS) {
            if (!packs.contains(pack)) {
                packs.add(Math.min(insertion, packs.size()), pack);
                insertion++;
            }
        }
    }
}
