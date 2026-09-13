package moddedmite.xylose.bettergamesetting.server.network;

import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.xylose.bettergamesetting.client.network.ServerStatusResponse;
import net.minecraft.ChatMessageComponent;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.Validate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetHandlerStatusServer {
    private static final Logger LOGGER = Logger.getLogger("BetterGameSetting");
    private static final String FAVICON_PREFIX = "data:image/png;base64,";
    private static final int PROTOCOL_VERSION = 78;
    private static final int PACKET_SERVER_QUERY = 0;
    private static final int PACKET_PING = 1;
    private static final int MAX_FRAME_LENGTH = 1024;

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private boolean handled;

    public NetHandlerStatusServer(Socket socket, DataInputStream in) throws IOException {
        this.socket = socket;
        this.in = in;
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void processServerQuery() throws IOException {
        PacketByteBuf packet = this.readFrame();
        if (packet.readVarInt() != PACKET_SERVER_QUERY) {
            this.closeChannel();
            return;
        }
        if (this.handled) {
            this.closeChannel();
            return;
        }
        this.handled = true;
        this.sendServerInfo();
    }

    public void processPing() throws IOException {
        PacketByteBuf packet = this.readFrame();
        if (packet.readVarInt() != PACKET_PING) {
            this.closeChannel();
            return;
        }
        long clientTime = packet.readLong();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        PacketByteBuf pong = PacketByteBuf.out(new DataOutputStream(body));
        pong.writeVarInt(PACKET_PING);
        pong.writeLong(clientTime);
        this.writeFrame(body.toByteArray());
        this.closeChannel();
    }

    private void sendServerInfo() throws IOException {
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        PacketByteBuf info = PacketByteBuf.out(new DataOutputStream(body));
        info.writeVarInt(PACKET_SERVER_QUERY);
        info.writeString(buildStatusResponse().toJson());
        this.writeFrame(body.toByteArray());
    }

    private PacketByteBuf readFrame() throws IOException {
        int length = PacketByteBuf.in(this.in).readVarInt();
        if (length < 0 || length > MAX_FRAME_LENGTH) {
            throw new IOException("Bad frame length " + length);
        }
        byte[] payload = new byte[length];
        this.in.readFully(payload);
        return PacketByteBuf.in(new DataInputStream(new ByteArrayInputStream(payload)));
    }

    private void writeFrame(byte[] body) throws IOException {
        PacketByteBuf frame = PacketByteBuf.out(this.out);
        frame.writeVarInt(body.length);
        this.out.write(body);
        this.out.flush();
    }

    private void closeChannel() {
        try {
            this.socket.close();
        } catch (IOException ignored) {
        }
    }

    private static ServerStatusResponse buildStatusResponse() {
        ServerStatusResponse response = new ServerStatusResponse();
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) {
            return response;
        }
        response.setServerDescription(ChatMessageComponent.createFromText(server.getMOTD()));
        response.setPlayers(new ServerStatusResponse.Players(server.getMaxPlayers(), server.getCurrentPlayerCount()));
        response.setVersion(new ServerStatusResponse.Version(server.getMinecraftVersion(), PROTOCOL_VERSION));
        applyServerIconToResponse(response, server);
        return response;
    }

    private static void applyServerIconToResponse(ServerStatusResponse response, MinecraftServer server) {
        File iconFile = server.getFile("server-icon.png");
        if (!iconFile.isFile()) {
            iconFile = new File(server.getFile(server.getFolderName()), "icon.png");
        }
        if (!iconFile.isFile()) {
            return;
        }
        try {
            BufferedImage image = ImageIO.read(iconFile);
            Validate.validState(image.getWidth() == 64, "Must be 64 pixels wide");
            Validate.validState(image.getHeight() == 64, "Must be 64 pixels high");
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", png);
            response.setFavicon(FAVICON_PREFIX + Base64.getEncoder().encodeToString(png.toByteArray()));
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Couldn't load server icon", exception);
        }
    }
}
