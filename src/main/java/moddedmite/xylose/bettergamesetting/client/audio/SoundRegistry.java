package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.RegistrySimple;

import java.util.Set;

public class SoundRegistry extends RegistrySimple {
    public void registerSound(SoundEventAccessorComposite sound) {
        this.putObject(sound.getSoundEventLocation(), sound);
    }

    public void clear() {
        this.registryObjects.clear();
    }

    public Set getKeys() {
        return this.registryObjects.keySet();
    }

    public boolean containsKey(Object key) {
        return this.registryObjects.containsKey(key);
    }
}
