package com.jamierf.wol;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public final class WakeOnLan {

    private static final String BROADCAST_ADDRESS = "255.255.255.255";
    private static final int WOL_PORT = 9;

    public static void wake(final String mac) throws IOException, DecoderException {
        wake(mac, WOL_PORT);
    }

    public static void wake(final String mac, final int port) throws IOException, DecoderException {
        final byte[] address = Hex.decodeHex(mac.replaceAll("[\\-:]", "").toCharArray());
        final byte[] payload = buildPayload(address);

        final SocketAddress destination = new InetSocketAddress(BROADCAST_ADDRESS, port);
        final DatagramPacket packet = new DatagramPacket(payload, payload.length, destination);

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.send(packet);
        }
    }

    private static byte[] buildPayload(final byte[] address) {
        final ByteBuffer packet = ByteBuffer.wrap(new byte[6 + (16 * address.length)]);
        for (int i = 0; i < 6; i++) {
            packet.put((byte) 0xff);
        }

        while (packet.remaining() >= address.length) {
            packet.put(address);
        }

        return packet.array();
    }

    private WakeOnLan() {}
}
