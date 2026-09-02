package moddedmite.xylose.bettergamesetting.client.audio;

import com.google.common.collect.Maps;
import net.minecraft.RegistrySimple;
import net.minecraft.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class SoundRegistry extends RegistrySimple {
    private Map<ResourceLocation, SoundEventAccessor> soundRegistry;

    public void registerSound(SoundEventAccessor sound) {
        this.putObject(sound.getLocation(), sound);
    }

    protected Map<ResourceLocation, SoundEventAccessor> createUnderlyingMap() {
        this.soundRegistry = Maps.<ResourceLocation, SoundEventAccessor>newHashMap();
        return this.soundRegistry;
    }
    
    
    public void clear() {
        this.registryObjects.clear();
    }

    public Set<ResourceLocation> getKeys() {
        return this.registryObjects.keySet();
    }

    public boolean containsKey(Object key) {
        return this.registryObjects.containsKey(key);
    }
}
