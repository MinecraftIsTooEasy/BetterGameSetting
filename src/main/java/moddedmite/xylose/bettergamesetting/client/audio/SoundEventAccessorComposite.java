package moddedmite.xylose.bettergamesetting.client.audio;

import com.google.common.collect.Lists;
import net.minecraft.ResourceLocation;
import net.minecraft.SoundPoolEntry;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SoundEventAccessorComposite implements ISoundEventAccessor {
    /**
     * A composite (List) of ISoundEventAccessors
     */
    private final List soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation id;
    private final SoundCategory field_148732_d;
    private double eventPitch;
    private double eventVolume;
    
    public SoundEventAccessorComposite(ResourceLocation p_i45120_1_, double p_i45120_2_, double p_i45120_4_, SoundCategory p_i45120_6_) {
        this.id = p_i45120_1_;
        this.eventVolume = p_i45120_4_;
        this.eventPitch = p_i45120_2_;
        this.field_148732_d = p_i45120_6_;
    }
    
    public int func_148721_a() {
        int i = 0;
        ISoundEventAccessor isoundeventaccessor;
        
        for (Iterator iterator = this.soundPool.iterator(); iterator.hasNext(); i += isoundeventaccessor.func_148721_a()) {
            isoundeventaccessor = (ISoundEventAccessor) iterator.next();
        }
        
        return i;
    }
    
    public SoundPoolEntry func_148720_g() {
        int i = this.func_148721_a();
        
        if (!this.soundPool.isEmpty() && i != 0) {
            int j = this.rnd.nextInt(i);
            Iterator iterator = this.soundPool.iterator();
            ISoundEventAccessor isoundeventaccessor;
            
            do {
                if (!iterator.hasNext()) {
                    return SoundHandler.MISSING_SOUND;
                }
                
                isoundeventaccessor = (ISoundEventAccessor) iterator.next();
                j -= isoundeventaccessor.func_148721_a();
            }
            while (j >= 0);
            
            SoundPoolEntry soundpoolentry = (SoundPoolEntry) isoundeventaccessor.func_148720_g();
            soundpoolentry.setPitch(/*soundpoolentry.getPitch() */ this.eventPitch);
            soundpoolentry.setVolume(/*soundpoolentry.getVolume() */ this.eventVolume);
            return soundpoolentry;
        } else {
            return SoundHandler.MISSING_SOUND;
        }
    }
    
    public void addSoundToEventPool(ISoundEventAccessor p_148727_1_) {
        this.soundPool.add(p_148727_1_);
    }
    
    public ResourceLocation getSoundEventLocation() {
        return this.id;
    }
    
    public SoundCategory getSoundCategory() {
        return this.field_148732_d;
    }
}