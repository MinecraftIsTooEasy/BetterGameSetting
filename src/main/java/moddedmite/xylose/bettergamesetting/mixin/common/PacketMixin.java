package moddedmite.xylose.bettergamesetting.mixin.common;

import moddedmite.xylose.bettergamesetting.server.network.NetHandlerHandshakeTCP;
import net.minecraft.ILogAgent;
import net.minecraft.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Mixin(Packet.class)
public class PacketMixin {
    @Unique private static final Set<Socket> handledConnections = Collections.newSetFromMap(new WeakHashMap<>());

    @Redirect(method = "readPacket", at = @At(value = "INVOKE", target = "Ljava/io/DataInput;readUnsignedByte()I"))
    private static int readPacketId(DataInput dataInput, ILogAgent logAgent, DataInput packetData, boolean isServerHandler, Socket socket) throws IOException {
        int packetId = dataInput.readUnsignedByte();
        if (!isServerHandler || socket == null) {
            return packetId;
        }
        synchronized (handledConnections) {
            if (!handledConnections.add(socket)) {
                return packetId;
            }
        }
        if (packetId == 2 || packetId == 254) {
            return packetId;
        }
        try {
            NetHandlerHandshakeTCP.processHandshake((DataInputStream) dataInput, socket, packetId);
            logAgent.logInfo("Answered modern server status ping from " + socket);
        } catch (Exception exception) {
            logAgent.logWarningException("Failed to answer modern server status ping from " + socket, exception);
        }
        throw new EOFException();
    }
}
