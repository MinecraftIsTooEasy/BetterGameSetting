package moddedmite.xylose.bettergamesetting.server.network;

import moddedmite.rustedironcore.network.PacketByteBuf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class NetHandlerHandshakeTCP {
    private static final int PACKET_HANDSHAKE = 0;
    private static final int STATE_STATUS = 1;
    private static final int STATE_LOGIN = 2;
    private static final int MAX_FRAME_LENGTH = 1024;

    public static void processHandshake(DataInputStream in, Socket socket, int firstByte) throws IOException {
        int frameLength = readVarInt(in, firstByte);
        if (frameLength < 0 || frameLength > MAX_FRAME_LENGTH) {
            closeQuietly(socket);
            return;
        }
        byte[] payload = new byte[frameLength];
        in.readFully(payload);
        PacketByteBuf handshake = PacketByteBuf.in(new DataInputStream(new ByteArrayInputStream(payload)));
        if (handshake.readVarInt() != PACKET_HANDSHAKE) {
            closeQuietly(socket);
            return;
        }
        handshake.readVarInt();
        handshake.readString();
        handshake.readUnsignedShort();
        int requestedState = handshake.readVarInt();
        if (requestedState == STATE_STATUS) {
            NetHandlerStatusServer handler = new NetHandlerStatusServer(socket, in);
            handler.processServerQuery();
            handler.processPing();
        } else if (requestedState == STATE_LOGIN) {
            closeQuietly(socket);
        } else {
            throw new UnsupportedOperationException("Invalid intention " + requestedState);
        }
    }

    private static int readVarInt(DataInputStream in, int firstByte) throws IOException {
        int result = 0;
        int position = 0;
        int current = firstByte;
        while (true) {
            result |= (current & 127) << position;
            if ((current & 128) == 0) {
                return result;
            }
            position += 7;
            if (position >= 35) {
                throw new IOException("VarInt too big");
            }
            current = in.readUnsignedByte();
        }
    }

    private static void closeQuietly(Socket socket) {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
