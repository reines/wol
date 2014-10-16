package com.jamierf.wol;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class WakeOnLan {

    private static final String BROADCAST_ADDRESS = "255.255.255.255";
    public static final int PORT = 9;

    public static void wake(final String mac) throws IOException {
        wake(mac, PORT);
    }

    public static void wake(final String mac, final int port) throws IOException {
        final byte[] address = decodeMacAddress(mac);
        final byte[] payload = buildPayload(address);

        final SocketAddress destination = new InetSocketAddress(BROADCAST_ADDRESS, port);
        final DatagramPacket packet = new DatagramPacket(payload, payload.length, destination);

        final DatagramSocket socket = new DatagramSocket();
        try {
            socket.setBroadcast(true);
            socket.send(packet);
        }
        finally {
            socket.close();
        }
    }

    protected static byte[] decodeMacAddress(final String mac) {
        final char[] chars = mac.replaceAll("[\\-: ]", "").toCharArray();
        if (chars.length != 12) {
            throw new IllegalArgumentException("Illegal length mac address: " + mac);
        }

        try {
            return Hex.decodeHex(chars);
        }
        catch (DecoderException e) {
            throw new IllegalArgumentException("Illegal non-hex mac address: " + mac, e);
        }
    }

    protected static byte[] buildPayload(final byte[] address) {
        final ByteBuffer packet = ByteBuffer.wrap(new byte[6 + (16 * address.length)]);
        for (int i = 0; i < 6; i++) {
            packet.put((byte) 0xff);
        }

        while (packet.remaining() >= address.length) {
            packet.put(address);
        }

        return packet.array();
    }
}
