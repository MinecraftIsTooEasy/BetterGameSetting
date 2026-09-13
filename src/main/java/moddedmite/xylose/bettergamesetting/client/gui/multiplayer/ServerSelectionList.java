package moddedmite.xylose.bettergamesetting.client.gui.multiplayer;

import moddedmite.xylose.bettergamesetting.api.IGuiMultiplayer;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.LanServer;
import net.minecraft.Minecraft;
import net.minecraft.ServerList;

import java.util.ArrayList;
import java.util.List;

public class ServerSelectionList extends GuiListExtended {
    private final IGuiMultiplayer owner;
    private final List<ServerListEntryNormal> serverListInternet = new ArrayList<>();
    private final List<ServerListEntryLanDetected> serverListLan = new ArrayList<>();
    private final GuiListExtended.IGuiListEntry lanScanEntry;
    private int selectedSlotIndex = -1;

    public ServerSelectionList(IGuiMultiplayer owner, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(mc, width, height, top, bottom, slotHeight);
        this.owner = owner;
        this.lanScanEntry = new ServerListEntryLanScan(owner);
        this.centerListVertically = false;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        if (index < this.serverListInternet.size()) {
            return this.serverListInternet.get(index);
        }

        index -= this.serverListInternet.size();

        if (index == 0) {
            return this.lanScanEntry;
        }

        return this.serverListLan.get(--index);
    }

    @Override
    protected int getSize() {
        return this.serverListInternet.size() + 1 + this.serverListLan.size();
    }

    public void setSelectedSlotIndex(int index) {
        this.selectedSlotIndex = index;
    }

    public boolean isLanScanEntry(int index) {
        return index >= 0 && index < this.getSize() && this.getListEntry(index) == this.lanScanEntry;
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }

    public int getSelected() {
        return this.selectedSlotIndex;
    }

    public void updateOnlineServers(ServerList list) {
        this.serverListInternet.clear();

        for (int i = 0; i < list.countServers(); ++i) {
            this.serverListInternet.add(new ServerListEntryNormal(this.owner, list.getServerData(i)));
        }
    }

    public void updateNetworkServers(List<LanServer> list) {
        this.serverListLan.clear();

        for (LanServer server : list) {
            this.serverListLan.add(new ServerListEntryLanDetected(this.owner, server));
        }
    }

    @Override
    protected void drawBackground() {
//        this.client.currentScreen.drawDefaultBackground();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        int k = this.top + 4 - this.getAmountScrolled() + slotIndex * this.slotHeight + this.headerPadding;
        this.getListEntry(slotIndex).mousePressed(slotIndex, mouseX, mouseY, 0, mouseX - j, mouseY - k);
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 40;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 85;
    }
}
