package moddedmite.xylose.bettergamesetting.client.audio;

public interface ISoundEventAccessor<T> {
    int getWeight();
    T cloneEntry();
}