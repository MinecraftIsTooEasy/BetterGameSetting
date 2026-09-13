package moddedmite.xylose.bettergamesetting.mixin.client.gui;

import moddedmite.xylose.bettergamesetting.api.IGuiMultiplayer;
import moddedmite.xylose.bettergamesetting.client.gui.multiplayer.ServerListEntryNormal;
import moddedmite.xylose.bettergamesetting.client.gui.multiplayer.ServerSelectionList;
import moddedmite.xylose.bettergamesetting.client.network.ServerPinger;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.minecraft.EnumChatFormatting;
import net.minecraft.ExternalTexture;
import net.minecraft.Gui;
import net.minecraft.GuiButton;
import net.minecraft.GuiMultiplayer;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.LanServer;
import net.minecraft.Minecraft;
import net.minecraft.ServerData;
import net.minecraft.ServerList;
import net.minecraft.Tessellator;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@Mixin(GuiMultiplayer.class)
public abstract class GuiMultiplayerMixin extends GuiScreen implements IGuiMultiplayer {
    @Shadow private ServerList internetServerList;
    @Shadow private int selectedServer;
    @Shadow private GuiButton field_96289_p;
    @Shadow private GuiButton buttonSelect;
    @Shadow private GuiButton buttonDelete;
    @Shadow private String lagTooltip;
    @Shadow private List listofLanServers;
    @Shadow private boolean info_showing;
    @Shadow private static int threadsPending;
    @Shadow private static Object lock;
    @Shadow private static ExternalTexture server_image_texture;

    @Shadow public abstract void updateButtonsForSelection();
    @Shadow protected abstract void drawServerInfoText(int margin_x, ServerData sd);
    @Shadow protected abstract void connectToServer(ServerData par1ServerData);
    @Shadow static void func_82291_a(ServerData par0ServerData) throws IOException {}

    @Unique private ServerSelectionList serverSelectionList;
    @Unique private final ServerPinger oldServerPinger = new ServerPinger();
    
    @Inject(method = "initGui", at = @At("TAIL"))
    private void createServerSelectionList(CallbackInfo ci) {
        this.serverSelectionList = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.serverSelectionList.updateOnlineServers(this.internetServerList);
        this.serverSelectionList.updateNetworkServers(this.listofLanServers);
        this.serverSelectionList.setSelectedSlotIndex(this.selectedServer);
    }

    @Inject(method = "updateScreen", at = @At("TAIL"))
    private void updateNetworkServers(CallbackInfo ci) {
        if (this.serverSelectionList != null) {
            this.serverSelectionList.updateNetworkServers(this.listofLanServers);
        }
//        this.oldServerPinger.pingPendingNetworks();
    }
    
//    @Inject(method = "onGuiClosed", at = @At("TAIL"))
//    private void closeTail(CallbackInfo ci) {
//        this.oldServerPinger.clearPendingNetworks();
//    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void drawScreen(int par1, int par2, float par3) {
        Minecraft.clearWorldSessionClientData();
        this.lagTooltip = null;
        this.drawDefaultBackground();
        if (this.info_showing) {
            this.drawDarkenedBackground(1);
            ServerData sd = this.internetServerList.getServerData(this.selectedServer);
            int margin_x = this.width / 2 - 154;
            if (server_image_texture != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, server_image_texture.getGlTextureId());
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_F(0.7F, 0.7F, 0.7F);
                tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, 1.0D);
                tessellator.addVertexWithUV(this.width, this.height, 0.0D, 1.0D, 1.0D);
                tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, 1.0D, 0.0D);
                tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
            }
            this.drawServerInfoText(margin_x, sd);
            this.drawDarkenedBackground(2);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_BLEND);
        } else if (this.serverSelectionList != null) {
            this.serverSelectionList.drawScreen(par1, par2, par3);
        }
        this.drawCenteredString(this.fontRenderer, I18n.getString("multiplayer.title"), this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(par1, par2, par3);
        if (this.lagTooltip != null) {
            this.func_74007_a(this.lagTooltip, par1, par2);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void joinServer(int par1) {
        Minecraft.theMinecraft.increment_joinMultiplayerStat_asap = true;
        if (par1 < this.internetServerList.countServers()) {
            this.connectToServer(this.internetServerList.getServerData(par1));
        } else if ((par1 -= this.internetServerList.countServers() + 1) < this.listofLanServers.size()) {
            LanServer var2 = (LanServer) this.listofLanServers.get(par1);
            this.connectToServer(new ServerData(var2.getServerMotd(), var2.getServerIpPort()));
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void func_74007_a(String par1Str, int par2, int par3) {
        if (par1Str != null) {
            ScreenUtil.getInstance().drawTooltip(par1Str, par2, par3);
        }
    }

    @Override
    public void selectServer(int index) {
        this.selectedServer = index;
        if (this.serverSelectionList != null) {
            this.serverSelectionList.setSelectedSlotIndex(index);
        }

        boolean isScan = this.serverSelectionList != null && this.serverSelectionList.isLanScanEntry(index);
        boolean isInternet = !isScan && index >= 0 && index < this.internetServerList.countServers();
        ServerData data = isInternet ? this.internetServerList.getServerData(index) : null;
        this.buttonSelect.enabled = !isScan && index >= 0 && index < this.internetServerList.countServers() + this.listofLanServers.size() + 1 && (data == null || data.field_82821_f == 78);
        this.field_96289_p.enabled = isInternet;
        this.buttonDelete.enabled = isInternet;
        this.updateButtonsForSelection();
        GuiMultiplayer.loadServerImage(isScan || data == null || data.image_url == null ? null : FilenameUtils.getName(data.image_url));
    }

    @Override
    public boolean canMoveUp(ServerListEntryNormal entry, int slotIndex) {
        return slotIndex > 0;
    }

    @Override
    public boolean canMoveDown(ServerListEntryNormal entry, int slotIndex) {
        return slotIndex < this.internetServerList.countServers() - 1;
    }

    @Override
    public void moveServerUp(ServerListEntryNormal entry, int slotIndex, boolean shift) {
        int i = shift ? 0 : slotIndex - 1;
        this.internetServerList.swapServers(slotIndex, i);

        if (this.serverSelectionList.getSelected() == slotIndex) {
            this.selectServer(i);
        }

        this.serverSelectionList.updateOnlineServers(this.internetServerList);
    }

    @Override
    public void moveServerDown(ServerListEntryNormal entry, int slotIndex, boolean shift) {
        int i = shift ? this.internetServerList.countServers() - 1 : slotIndex + 1;
        this.internetServerList.swapServers(slotIndex, i);

        if (this.serverSelectionList.getSelected() == slotIndex) {
            this.selectServer(i);
        }

        this.serverSelectionList.updateOnlineServers(this.internetServerList);
    }

    @Override
    public void connectToSelected() {
        this.joinServer(this.selectedServer);
    }

    @Override
    public void pingServer(ServerData server) {
        synchronized (lock) {
            if (threadsPending < 5 && !server.field_78841_f) {
                server.field_78841_f = true;
                server.pingToServer = -2L;
                server.serverMOTD = "";
                server.populationInfo = "";
                ++threadsPending;

                new Thread(() -> {
                    ServerPinger pinger = new ServerPinger();
                    try {
                        pinger.ping(server);
                    } catch (UnknownHostException e) {
                        server.pingToServer = -1L;
                        server.serverMOTD = EnumChatFormatting.DARK_RED + I18n.getString("multiplayer.status.cannot_resolve");
                    } catch (Exception e) {
                        server.pingToServer = -1L;
                        server.serverMOTD = EnumChatFormatting.DARK_RED + I18n.getString("multiplayer.status.cannot_connect");
                        server.populationInfo = "";
                        pinger.tryCompatibilityPing(server);
                    } finally {
                        synchronized (lock) {
                            --threadsPending;
                        }
                    }
                }, "Server Pinger").start();
            }
        }
    }
    
    public ServerPinger getOldServerPinger() {
        return this.oldServerPinger;
    }
    
    
    @Override
    public void setHoveringText(String text) {
        this.lagTooltip = text;
    }

    @Override
    public ServerList getServerList() {
        return this.internetServerList;
    }

    @Unique
    private void drawDarkenedBackground(int layer) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int left = this.serverSelectionList.left;
        int right = this.serverSelectionList.right;
        int top = this.serverSelectionList.top;
        int bottom = this.serverSelectionList.bottom;

        if (layer == 1) {
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(0x202020);
            tessellator.addVertexWithUV(left, bottom, 0.0D, (float) left / 32.0F, (float) bottom / 32.0F);
            tessellator.addVertexWithUV(right, bottom, 0.0D, (float) right / 32.0F, (float) bottom / 32.0F);
            tessellator.addVertexWithUV(right, top, 0.0D, (float) right / 32.0F, (float) top / 32.0F);
            tessellator.addVertexWithUV(left, top, 0.0D, (float) left / 32.0F, (float) top / 32.0F);
            tessellator.draw();
        } else if (layer == 2) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.overlayBackground(0, top, 255, 255);
            this.overlayBackground(bottom, this.height, 255, 255);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertexWithUV(left, top + 4, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, top + 4, 0.0D, 1.0D, 1.0D);
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertexWithUV(right, bottom - 4, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, bottom - 4, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }
    }

    @Unique
    private void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x404040, endAlpha);
        tessellator.addVertexWithUV(0.0D, endY, 0.0D, 0.0D, (float) endY / 32.0F);
        tessellator.addVertexWithUV(this.width, endY, 0.0D, (float) this.width / 32.0F, (float) endY / 32.0F);
        tessellator.setColorRGBA_I(0x404040, startAlpha);
        tessellator.addVertexWithUV(this.width, startY, 0.0D, (float) this.width / 32.0F, (float) startY / 32.0F);
        tessellator.addVertexWithUV(0.0D, startY, 0.0D, 0.0D, (float) startY / 32.0F);
        tessellator.draw();
    }
}
