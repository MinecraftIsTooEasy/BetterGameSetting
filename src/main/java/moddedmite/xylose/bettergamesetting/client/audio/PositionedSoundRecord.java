package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.ResourceLocation;
import net.minecraft.Vec3;

public class PositionedSoundRecord extends PositionedSound {
    public PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, Vec3 pos) {
        this(soundIn, categoryIn, volumeIn, pitchIn, (float) pos.xCoord + 0.5F, (float) pos.yCoord + 0.5F, (float) pos.zCoord + 0.5F);
    }
    
    public static PositionedSoundRecord getMasterRecord(SoundEvent soundIn, float pitchIn) {
        return getRecord(soundIn, pitchIn, 0.25F);
    }
    
    public static PositionedSoundRecord getRecord(SoundEvent soundIn, float pitchIn, float volumeIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.MASTER, volumeIn, pitchIn, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }
    
    public static PositionedSoundRecord getMusicRecord(SoundEvent soundIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord of(ResourceLocation soundId) {
        return new PositionedSoundRecord(soundId, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }
    
    public static PositionedSoundRecord getRecordSoundRecord(SoundEvent soundIn, float xIn, float yIn, float zIn) {
        return new PositionedSoundRecord(soundIn, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }
    
    public PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, float xIn, float yIn, float zIn) {
        this(soundIn, categoryIn, volumeIn, pitchIn, false, 0, ISound.AttenuationType.LINEAR, xIn, yIn, zIn);
    }
    
    private PositionedSoundRecord(SoundEvent soundIn, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn, float zIn) {
        this(soundIn.soundName(), categoryIn, volumeIn, pitchIn, repeatIn, repeatDelayIn, attenuationTypeIn, xIn, yIn, zIn);
    }
    
    public PositionedSoundRecord(ResourceLocation soundId, SoundCategory categoryIn, float volumeIn, float pitchIn, boolean repeatIn, int repeatDelayIn, ISound.AttenuationType attenuationTypeIn, float xIn, float yIn, float zIn) {
        super(soundId, categoryIn);
        this.volume = volumeIn;
        this.pitch = pitchIn;
        this.xPosF = xIn;
        this.yPosF = yIn;
        this.zPosF = zIn;
        this.repeat = repeatIn;
        this.repeatDelay = repeatDelayIn;
        this.attenuationType = attenuationTypeIn;
    }
}