package moddedmite.xylose.bettergamesetting.client.audio;

public abstract class MovingSound extends PositionedSound implements ITickableSound {
    protected boolean donePlaying = false;
    
    protected MovingSound(SoundEvent soundIn, SoundCategory categoryIn)
    {
        super(soundIn, categoryIn);
    }
    
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}