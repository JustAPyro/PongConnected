package com.pyredevelopment.pongonline;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PongGame {

    private String playerOneID = null;
    private String playerTwoID = null;
    private double PlayerOnePosition;
    private double PlayerTwoPosition;
    private long p1LastUpdate;
    private long p2LastUpdate;

    private final double speed = 1;


    public PongGame() {
        PlayerOnePosition = 240;
        PlayerTwoPosition = 240;
        p1LastUpdate = System.currentTimeMillis();
        p2LastUpdate = System.currentTimeMillis();
    }

    public void updateState(String clientID, String update) {

        if (playerOneID == null)
            playerOneID = clientID;
        else if (playerTwoID == null && !playerOneID.equals(clientID))
            playerTwoID = clientID;


        char[] updateBits = update.toCharArray();
        boolean wPress = updateBits[1] != '0';
        boolean sPress = updateBits[2] != '0';
        long currentTime = System.currentTimeMillis();


        if (clientID.equals(playerOneID)) {
            if (wPress ^ sPress) {
                int directionMultiplier = wPress ? -1 : 1;
                PlayerOnePosition += ((currentTime - p1LastUpdate) * speed) * directionMultiplier;
            }
            p1LastUpdate = currentTime;
        }
        else if (clientID.equals(playerTwoID)){
            if (wPress ^ sPress) {
                int directionMultiplier = wPress ? 1 : -1;
                PlayerTwoPosition += ((currentTime - p1LastUpdate) * speed) * directionMultiplier;
            }
            p2LastUpdate = currentTime;
        }

        //System.out.println("P1Pos: " + PlayerOnePosition + " | P2Pos: " + PlayerTwoPosition);
    }

    public static short[] decodeState(byte[] incomingBytes) {

        ByteBuffer p1Buf = ByteBuffer.allocate(2);
        ByteBuffer p2Buf = ByteBuffer.allocate(2);
        p1Buf.order(ByteOrder.LITTLE_ENDIAN);
        p2Buf.order(ByteOrder.LITTLE_ENDIAN);

        p1Buf.put(incomingBytes[0]);
        p1Buf.put(incomingBytes[1]);
        p2Buf.put(incomingBytes[2]);
        p2Buf.put(incomingBytes[3]);

        p1Buf.flip();
        p2Buf.flip();

        short[] shortPositions = new short[2];
        shortPositions[0] = p1Buf.getShort();
        shortPositions[1] = p2Buf.getShort();

        return shortPositions;

    }


    public byte[] getState() {
        ByteBuffer p1Buf = ByteBuffer.allocate(2);
        ByteBuffer p2Buf = ByteBuffer.allocate(2);
        p1Buf.order(ByteOrder.LITTLE_ENDIAN);
        p2Buf.order(ByteOrder.LITTLE_ENDIAN);

        short p1Short = (short) Math.round(PlayerOnePosition);
        short p2Short = (short) Math.round(PlayerTwoPosition);

        p1Buf.putShort(p1Short);
        p2Buf.putShort(p2Short);

        byte[] p1Byte = new byte[2];
        byte[] p2Byte = new byte[2];

        p1Buf.flip();
        p2Buf.flip();

        p1Buf.get(p1Byte);
        p2Buf.get(p2Byte);

        byte[] finalByte = new byte[4];
        finalByte[0] = p1Byte[0];
        finalByte[1] = p1Byte[1];
        finalByte[2] = p2Byte[0];
        finalByte[3] = p2Byte[1];

        return finalByte;
    }


}
