package moddedmite.xylose.bettergamesetting.client.network;

import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.xylose.bettergamesetting.api.IServerData;
import moddedmite.xylose.bettergamesetting.init.BGSClient;
import net.minecraft.EnumChatFormatting;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.ServerAddress;
import net.minecraft.ServerData;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ServerPinger {
    private static final String PING_DATA_PREFIX = "data:image/png;base64,";

    public void ping(ServerData server) throws IOException {
        ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        server.serverMOTD = I18n.getString("multiplayer.status.pinging");
        server.pingToServer = -1L;

        Socket socket = new Socket();
        try {
            socket.setSoTimeout(3000);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(18);
            socket.connect(new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort()), 3000);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            ByteArrayOutputStream handshakeBytes = new ByteArrayOutputStream();
            PacketByteBuf handshake = PacketByteBuf.out(new DataOutputStream(handshakeBytes));
            handshake.writeVarInt(0);
            handshake.writeVarInt(78);
            handshake.writeString(serveraddress.getIP());
            handshake.writeShort(serveraddress.getPort());
            handshake.writeVarInt(1);
            writeFrame(out, handshakeBytes.toByteArray());

            ByteArrayOutputStream queryBytes = new ByteArrayOutputStream();
            PacketByteBuf.out(new DataOutputStream(queryBytes)).writeVarInt(0);
            writeFrame(out, queryBytes.toByteArray());
            out.flush();

            PacketByteBuf inBuf = PacketByteBuf.in(in);
            inBuf.readVarInt();
            inBuf.readVarInt();
            ServerStatusResponse serverstatusresponse = ServerStatusResponse.fromJson(inBuf.readString());

            if (serverstatusresponse.getServerDescription() != null) {
                server.serverMOTD = serverstatusresponse.getServerDescription().toStringWithFormatting(true);
            } else {
                server.serverMOTD = "";
            }

            if (serverstatusresponse.getVersion() != null) {
                server.gameVersion = serverstatusresponse.getVersion().name();
                server.field_82821_f = serverstatusresponse.getVersion().protocol();
            } else {
                server.gameVersion = I18n.getString("multiplayer.status.old");
                server.field_82821_f = 0;
            }

            if (serverstatusresponse.getPlayers() != null) {
                server.populationInfo = EnumChatFormatting.GRAY + "" + serverstatusresponse.getPlayers().onlinePlayerCount() + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + serverstatusresponse.getPlayers().maxPlayers();
            } else {
                server.populationInfo = EnumChatFormatting.DARK_GRAY + I18n.getString("multiplayer.status.unknown");
            }

            if (serverstatusresponse.getFavicon() != null) {
                String s = serverstatusresponse.getFavicon();

                if (s.startsWith(PING_DATA_PREFIX)) {
                    ((IServerData) server).setBase64EncodedIconData(s.substring(PING_DATA_PREFIX.length()));
                } else {
                    BGSClient.logger.warn("Invalid server icon (unknown format)");
                }
            } else {
                ((IServerData) server).setBase64EncodedIconData(null);
            }

            long pingSentAt = Minecraft.getSystemTime();
            ByteArrayOutputStream pingBytes = new ByteArrayOutputStream();
            PacketByteBuf ping = PacketByteBuf.out(new DataOutputStream(pingBytes));
            ping.writeVarInt(1);
            ping.writeLong(pingSentAt);
            writeFrame(out, pingBytes.toByteArray());
            out.flush();

            inBuf.readVarInt();
            inBuf.readVarInt();
            in.readLong();
            server.pingToServer = Minecraft.getSystemTime() - pingSentAt;
        } finally {
            closeQuietly(socket);
        }
    }

    public void tryCompatibilityPing(ServerData server) {
        ServerAddress serveraddress = ServerAddress.func_78860_a(server.serverIP);
        Socket socket = new Socket();
        try {
            socket.setSoTimeout(3000);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(18);
            socket.connect(new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort()), 3000);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeByte(254);
            out.writeByte(1);
            out.writeByte(250);
            char[] achar = "MC|PingHost".toCharArray();
            out.writeShort(achar.length);

            for (char c0 : achar) {
                out.writeChar(c0);
            }

            out.writeShort(7 + 2 * serveraddress.getIP().length());
            out.writeByte(127);
            achar = serveraddress.getIP().toCharArray();
            out.writeShort(achar.length);

            for (char c1 : achar) {
                out.writeChar(c1);
            }

            out.writeInt(serveraddress.getPort());
            out.flush();

            if (in.readUnsignedByte() != 255) {
                return;
            }

            byte[] data = new byte[in.readShort() * 2];
            in.readFully(data);
            String[] astring = new String(data, StandardCharsets.UTF_16BE).split("\u0000", 6);

            if ("§1".equals(astring[0])) {
                server.field_82821_f = -1;
                server.gameVersion = astring[2];
                server.serverMOTD = astring[3];
                server.populationInfo = EnumChatFormatting.GRAY + "" + parseInt(astring[4], -1) + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + parseInt(astring[5], -1);
            }
        } catch (EOFException | UnknownHostException ignored) {
        } catch (IOException e) {
	        BGSClient.logger.warn("Can't ping {}", server.serverIP, e);
        } finally {
            closeQuietly(socket);
        }
    }

    private static void writeFrame(DataOutputStream out, byte[] body) throws IOException {
        PacketByteBuf outBuf = PacketByteBuf.out(out);
        outBuf.writeVarInt(body.length);
        out.write(body);
    }

    private static void closeQuietly(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
