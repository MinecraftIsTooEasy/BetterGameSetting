package moddedmite.xylose.bettergamesetting.client.audio;

import net.minecraft.IUpdatePlayerListBox;

public interface ITickableSound extends ISound, IUpdatePlayerListBox {
    boolean isDonePlaying();
}