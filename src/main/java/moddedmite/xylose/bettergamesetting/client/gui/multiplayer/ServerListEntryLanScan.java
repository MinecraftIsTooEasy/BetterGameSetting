package moddedmite.xylose.bettergamesetting.client.gui.multiplayer;

import moddedmite.xylose.bettergamesetting.api.IGuiMultiplayer;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import net.minecraft.I18n;
import net.minecraft.Minecraft;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry {
    private final IGuiMultiplayer owner;
    private final Minecraft mc = Minecraft.getMinecraft();

    public ServerListEntryLanScan(IGuiMultiplayer owner) {
        this.owner = owner;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        int i = y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
        this.mc.fontRenderer.drawStringWithShadow(I18n.getString("lanServer.scanning"), this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(I18n.getString("lanServer.scanning")) / 2, i, 0xFFFFFF);
        String s = switch ((int) (Minecraft.getSystemTime() / 300L % 4L)) {
	        case 1, 3 -> "o O o";
	        case 2 -> "o o O";
	        default -> "O o o";
        };
	    
	    this.mc.fontRenderer.drawStringWithShadow(s, this.mc.currentScreen.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, i + this.mc.fontRenderer.FONT_HEIGHT, 0x808080);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        this.owner.selectServer(slotIndex);
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
}
