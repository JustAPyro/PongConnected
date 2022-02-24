package com.pyredevelopment.pongonline;

import java.util.BitSet;

public class ByteBuilder {

    private byte b;

    // Returns a bitset containing the values in bytes.
    public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public ByteBuilder() {
        b = 0;
    }



}
