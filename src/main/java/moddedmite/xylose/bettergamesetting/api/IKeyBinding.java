package moddedmite.xylose.bettergamesetting.api;

public interface IKeyBinding {
    default int getDefaultKey() {
        return 0;
    }
}
