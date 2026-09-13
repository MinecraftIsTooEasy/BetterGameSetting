package moddedmite.xylose.bettergamesetting.client.gui.multiplayer;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import moddedmite.xylose.bettergamesetting.api.IGuiMultiplayer;
import moddedmite.xylose.bettergamesetting.api.IServerData;
import moddedmite.xylose.bettergamesetting.client.gui.base.GuiListExtended;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.DynamicTexture;
import net.minecraft.EnumChatFormatting;
import net.minecraft.Gui;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.minecraft.ServerData;
import net.minecraft.TextureObject;
import org.apache.commons.lang3.Validate;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
    private static final ThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_pack.png");
    private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation(BGSClient.resourceId, "textures/gui/server_selection.png");
    private final IGuiMultiplayer owner;
    private final Minecraft mc;
    private final ServerData server;
    private final ResourceLocation serverIcon;
    private DynamicTexture icon;
    private String lastIconB64;
    private long lastClickTime;

    public ServerListEntryNormal(IGuiMultiplayer owner, ServerData server) {
        this.owner = owner;
        this.server = server;
        this.mc = Minecraft.getMinecraft();
        this.serverIcon = new ResourceLocation(BGSClient.resourceId, "servers/" + server.serverIP + "/icon", false);

        TextureObject existing = this.mc.getTextureManager().getTexture(this.serverIcon);
        this.icon = existing instanceof DynamicTexture ? (DynamicTexture) existing : null;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
        if (!this.server.field_78841_f) {
            this.owner.pingServer(this.server);
        }

        boolean flag = this.server.field_82821_f > 78;
        boolean flag1 = this.server.field_82821_f < 78;
        boolean flag2 = flag || flag1;

        this.mc.fontRenderer.drawStringWithShadow(this.server.serverName, x + 35, y + 1, 0xFFFFFF);
        this.mc.fontRenderer.drawStringWithShadow(this.server.serverMOTD, x + 35, y + 12, 0x808080);
        this.mc.fontRenderer.drawStringWithShadow(this.server.populationInfo, x + listWidth - this.mc.fontRenderer.getStringWidth(this.server.populationInfo) - 15 - 2, y + 12, 0x808080);

        if (flag2) {
            String s = EnumChatFormatting.DARK_RED + this.server.gameVersion;
            this.mc.fontRenderer.drawStringWithShadow(s, x + listWidth - this.mc.fontRenderer.getStringWidth(s) - 15 - 2, y + 1, 0x808080);
        }

        if (!this.mc.gameSettings.hideServerAddress && !this.server.isHidingAddress()) {
            this.mc.fontRenderer.drawStringWithShadow(this.server.serverIP, x + 35, y + 23, 0x303030);
        } else {
            this.mc.fontRenderer.drawStringWithShadow(I18n.getString("selectServer.hiddenAddress"), x + 35, y + 23, 0x303030);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int k = 0;
        int l;
        String s1;

        if (flag2) {
            l = 5;
            s1 = I18n.getString("multiplayer.status.incompatible");
        } else if (this.server.field_78841_f && this.server.pingToServer != -2L) {
            if (this.server.pingToServer < 0L) {
                l = 5;
            } else if (this.server.pingToServer < 150L) {
                l = 0;
            } else if (this.server.pingToServer < 300L) {
                l = 1;
            } else if (this.server.pingToServer < 600L) {
                l = 2;
            } else if (this.server.pingToServer < 1000L) {
                l = 3;
            } else {
                l = 4;
            }

            s1 = this.server.pingToServer < 0L ? I18n.getString("multiplayer.status.no_connection") : I18n.getStringParams("multiplayer.status.ping", this.server.pingToServer);
        } else {
            k = 1;
            l = (int) (Minecraft.getSystemTime() / 100L + (long) (slotIndex * 2) & 7L);

            if (l > 4) {
                l = 8 - l;
            }

            s1 = I18n.getString("multiplayer.status.pinging");
        }

        ScreenUtil.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, k * 10, 176 + l * 8, 10, 8, 256.0F, 256.0F);

        if (((IServerData) this.server).getBase64EncodedIconData() != null && !((IServerData) this.server).getBase64EncodedIconData().equals(this.lastIconB64)) {
            this.lastIconB64 = ((IServerData) this.server).getBase64EncodedIconData();
            this.prepareServerIcon();
            this.owner.getServerList().saveServerList();
        }
        
        if (this.icon != null) {
            this.drawTextureAt(x, y, this.serverIcon);
        } else {
            this.drawTextureAt(x, y, UNKNOWN_SERVER);
        }

        if (mouseX >= x + listWidth - 15 - 4 && mouseX <= x + listWidth - 15 + 10 + 4 && mouseY >= y - 4 && mouseY <= y + 8 + 4) {
            this.owner.setHoveringText(s1);
        }

        if (this.mc.gameSettings.touchscreen || isSelected) {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = mouseX - x;
            int l1 = mouseY - y;
            
            if (this.canJoin()) {
                if (k1 < 32 && k1 > 16) {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            
            if (this.owner.canMoveUp(this, slotIndex)) {
                if (k1 < 16 && l1 < 16) {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            
            if (this.owner.canMoveDown(this, slotIndex)) {
                if (k1 < 16 && l1 > 16) {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                } else {
                    ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }
    
    protected void drawTextureAt(int p_178012_1_, int p_178012_2_, ResourceLocation p_178012_3_) {
        this.mc.getTextureManager().bindTexture(p_178012_3_);
        GL11.glEnable(GL11.GL_BLEND);
        ScreenUtil.drawModalRectWithCustomSizedTexture(p_178012_1_, p_178012_2_, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawIcon(int x, int y) {
        this.updateIcon();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.icon != null ? this.serverIcon : UNKNOWN_SERVER);
        GL11.glEnable(GL11.GL_BLEND);
        ScreenUtil.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void updateIcon() {
        String iconData = ((IServerData) this.server).getBase64EncodedIconData();

        if (iconData == null) {
            if (this.icon != null) {
                ScreenUtil.deleteTexture(this.serverIcon, this.icon.getGlTextureId());
                this.icon = null;
            }
            this.lastIconB64 = null;
        } else if (!iconData.equals(this.lastIconB64)) {
            this.lastIconB64 = iconData;
            this.prepareServerIcon();
        }
    }

    private void prepareServerIcon() {
        BufferedImage bufferedimage;

        try {
            bufferedimage = ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(((IServerData) this.server).getBase64EncodedIconData())));
            Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
            Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
        } catch (Throwable throwable) {
            this.server.setBase64EncodedIconData(null);
            this.lastIconB64 = null;
            return;
        }

        if (this.icon == null) {
            this.icon = new DynamicTexture(bufferedimage.getWidth(), bufferedimage.getHeight());
            this.mc.getTextureManager().loadTexture(this.serverIcon, this.icon);
        }

        bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), this.icon.getTextureData(), 0, bufferedimage.getWidth());
        this.icon.updateDynamicTexture();
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        if (relativeX <= 32) {
            if (relativeX < 32 && relativeX > 16 && this.canJoin()) {
                this.owner.selectServer(slotIndex);
                this.owner.connectToSelected();
                return true;
            }

            if (relativeX < 16 && relativeY < 16 && this.owner.canMoveUp(this, slotIndex)) {
                this.owner.moveServerUp(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }

            if (relativeX < 16 && relativeY > 16 && this.owner.canMoveDown(this, slotIndex)) {
                this.owner.moveServerDown(this, slotIndex, GuiScreen.isShiftKeyDown());
                return true;
            }
        }

        this.owner.selectServer(slotIndex);

        if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
            this.owner.connectToSelected();
        }

        this.lastClickTime = Minecraft.getSystemTime();
        return false;
    }

    private boolean canJoin() {
        return true;
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

    public ServerData getServerData() {
        return this.server;
    }
}
