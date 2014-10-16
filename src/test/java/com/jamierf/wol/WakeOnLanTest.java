package com.jamierf.wol;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WakeOnLanTest {

    private static final int TEST_PORT = 43582;
    private static final int SOCKET_TIMEOUT = 1000; // 1 second

    @Test
    public void testDecodeMac() {
        final byte[] expected = {-12, -73, -30, -103, 61, 80};
        assertArrayEquals(expected, WakeOnLan.decodeMacAddress("F4:B7:E2:99:3D:50"));
        assertArrayEquals(expected, WakeOnLan.decodeMacAddress("F4 B7 E2 99 3D 50"));
        assertArrayEquals(expected, WakeOnLan.decodeMacAddress("F4-B7-E2-99-3D-50"));
        assertArrayEquals(expected, WakeOnLan.decodeMacAddress("F4B7E2993D50"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeEmptyMac() throws DecoderException {
        WakeOnLan.decodeMacAddress("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalidMac() throws DecoderException {
        WakeOnLan.decodeMacAddress("F4:B7:E2:99:3D:5K");
    }

    @Test
    public void testPayload() {
        final byte[] expected = {-1, -1, -1, -1, -1, -1, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80};
        final byte[] address = {-12, -73, -30, -103, 61, 80};
        final byte[] payload = WakeOnLan.buildPayload(address);
        assertArrayEquals(expected, payload);
    }

    @Test
    public void testBroadcast() throws IOException {
        final DatagramSocket socket = new DatagramSocket(TEST_PORT, InetAddress.getByName("0.0.0.0"));
        try {
            socket.setSoTimeout(SOCKET_TIMEOUT);

            WakeOnLan.wake("F4:B7:E2:99:3D:50", TEST_PORT);

            final byte[] buffer = new byte[512];
            final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            assertEquals(0, packet.getOffset());
            assertEquals(102, packet.getLength());

            final byte[] payload = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), payload, 0, packet.getLength());

            final byte[] expected = {-1, -1, -1, -1, -1, -1, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80, -12, -73, -30, -103, 61, 80};
            assertArrayEquals(expected, payload);
        }
        finally {
            socket.close();
        }
    }
}
