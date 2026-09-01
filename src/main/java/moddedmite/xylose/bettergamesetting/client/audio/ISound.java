package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.ResourceLocation;

public interface ISound {
    ResourceLocation getSoundLocation();
    boolean canRepeat();
    int getRepeatDelay();
    float getVolume();
    float getPitch();
    float getXPosF();
    float getYPosF();
    float getZPosF();
    AttenuationType getAttenuationType();

    public static enum AttenuationType {
        NONE(0),
        LINEAR(2);

        private final int type;

        private AttenuationType(int p_i45110_3_) {
            this.type = p_i45110_3_;
        }

        public int getTypeInt() {
            return this.type;
        }
    }
}