package moddedmite.xylose.bettergamesetting.client.gui.multiplayer;

import moddedmite.xylose.bettergamesetting.api.IGuiMultiplayer;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.I18n;
import net.minecraft.LanServer;
import net.minecraft.Minecraft;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
    private final IGuiMultiplayer owner;
    private final Minecraft mc;
    private final LanServer server;
    private long lastClickTime;

    public ServerListEntryLanDetected(IGuiMultiplayer owner, LanServer server) {
        this.owner = owner;
        this.server = server;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        this.mc.fontRenderer.drawStringWithShadow(I18n.getString("lanServer.title"), x + 2, y + 1, 0xFFFFFF);
        this.mc.fontRenderer.drawStringWithShadow(this.server.getServerMotd(), x + 2, y + 12, 0x808080);

        if (this.mc.gameSettings.hideServerAddress) {
            this.mc.fontRenderer.drawStringWithShadow(I18n.getString("selectServer.hiddenAddress"), x + 2, y + 23, 0x303030);
        } else {
            this.mc.fontRenderer.drawStringWithShadow(this.server.getServerIpPort(), x + 2, y + 23, 0x303030);
        }
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        this.owner.selectServer(slotIndex);

        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.owner.connectToSelected();
        }

        this.lastClickTime = Minecraft.getSystemTime();
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
    }

    @Override
    public void keyTyped(int slotIndex, char typedChar, int keyCode) {
    }

    @Override
    public void setSelected(int slotIndex, int mouseX, int mouseY) {
    }

    public LanServer getServerData() {
        return this.server;
    }
}
