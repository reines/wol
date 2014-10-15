package com.jamierf.wol;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class WakeOnLanTest {

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
}
