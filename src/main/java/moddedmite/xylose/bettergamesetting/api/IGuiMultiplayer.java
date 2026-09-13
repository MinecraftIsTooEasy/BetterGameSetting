package moddedmite.xylose.bettergamesetting.api;

import moddedmite.xylose.bettergamesetting.client.gui.multiplayer.ServerListEntryNormal;
import moddedmite.xylose.bettergamesetting.client.network.ServerPinger;
import net.minecraft.ServerData;
import net.minecraft.ServerList;

public interface IGuiMultiplayer {
    void selectServer(int index);

    void connectToSelected();

    void pingServer(ServerData server);

    void setHoveringText(String text);
    
    ServerList getServerList();
    
    boolean canMoveUp(ServerListEntryNormal entry, int slotIndex);

    boolean canMoveDown(ServerListEntryNormal entry, int slotIndex);

    void moveServerUp(ServerListEntryNormal entry, int slotIndex, boolean shift);

    void moveServerDown(ServerListEntryNormal entry, int slotIndex, boolean shift);
    
    ServerPinger getOldServerPinger();
}
