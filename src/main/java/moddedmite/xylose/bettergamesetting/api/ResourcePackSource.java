package moddedmite.xylose.bettergamesetting.api;

import java.io.File;

/** File backing a directory or ZIP resource pack; used for legacy resource discovery. */
public interface ResourcePackSource {

    File bgs$getSourceFile();
}
