package com.pyredevelopment.pongonline;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PongGame implements Runnable {

    // - - - - - - - - - - Static Variables - - - - - - - - - -

    // These variables determine how often certain method calls happen in game loop
    private final static double UPS = 60; // Updates per second
    private final static double PPS = 3;  // Pushes per second (Server)

    // Create a logger for this file to track its behavior.
    private final static Logger logger = LogManager.getLogger(PongGame.class);

    private String playerOneID = null;
    private String playerTwoID = null;
    private double PlayerOnePosition;
    private double PlayerTwoPosition;
    private double[] ballPosition;
    private long p1LastUpdate;
    private long p2LastUpdate;

    private final double speed = .75;

    private boolean loopRunning = false;


    public PongGame() {
        // Start a new game thread
        Thread gameThread = new Thread(this, "tGame");
        gameThread.start();
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
                int directionMultiplier = wPress ? -1 : 1;
                PlayerTwoPosition += ((currentTime - p2LastUpdate) * speed) * directionMultiplier;
            }
            p2LastUpdate = currentTime;
        }

        //System.out.println("P1Pos: " + PlayerOnePosition + " | P2Pos: " + PlayerTwoPosition);
    }

    // decode an incoming byte array
    public static short[] decodeState(byte[] incomingBytes) {

        System.out.println(incomingBytes[6] + " | " + incomingBytes[7]);

        ByteBuffer p1Buf = ByteBuffer.allocate(2);
        ByteBuffer p2Buf = ByteBuffer.allocate(2);
        ByteBuffer ballX = ByteBuffer.allocate(2);
        ByteBuffer ballY = ByteBuffer.allocate(2);

        p1Buf.order(ByteOrder.LITTLE_ENDIAN);
        p2Buf.order(ByteOrder.LITTLE_ENDIAN);
        ballX.order(ByteOrder.LITTLE_ENDIAN);
        ballY.order(ByteOrder.LITTLE_ENDIAN);

        p1Buf.put(incomingBytes[0]);
        p1Buf.put(incomingBytes[1]);
        p2Buf.put(incomingBytes[2]);
        p2Buf.put(incomingBytes[3]);
        ballX.put(incomingBytes[4]);
        ballX.put(incomingBytes[5]);
        ballY.put(incomingBytes[6]);
        ballY.put(incomingBytes[7]);

        p1Buf.flip();
        p2Buf.flip();
        ballX.flip();
        ballY.flip();

        short[] shortPositions = new short[4];
        shortPositions[0] = p1Buf.getShort();
        shortPositions[1] = p2Buf.getShort();
        shortPositions[2] = ballX.getShort();
        shortPositions[3] = ballY.getShort();


        return shortPositions;

    }

    // Get the state of the game as a byte array to send
    public byte[] getState() {
        ByteBuffer p1Buf = ByteBuffer.allocate(2);
        ByteBuffer p2Buf = ByteBuffer.allocate(2);
        ByteBuffer ballX = ByteBuffer.allocate(2);
        ByteBuffer ballY = ByteBuffer.allocate(2);
        p1Buf.order(ByteOrder.LITTLE_ENDIAN);
        p2Buf.order(ByteOrder.LITTLE_ENDIAN);
        ballX.order(ByteOrder.LITTLE_ENDIAN);
        ballY.order(ByteOrder.LITTLE_ENDIAN);

        short p1Short = (short) Math.round(PlayerOnePosition);
        short p2Short = (short) Math.round(PlayerTwoPosition);
        short ballXShort = (short) Math.round(ballPosition[0]);
        short ballYShort = (short) Math.round(ballPosition[1]);


        p1Buf.putShort(p1Short);
        p2Buf.putShort(p2Short);
        ballX.putShort(ballXShort);
        ballY.putShort(ballYShort);

        byte[] p1Byte = new byte[2];
        byte[] p2Byte = new byte[2];
        byte[] bxByte = new byte[2];
        byte[] byByte = new byte[2];

        p1Buf.flip();
        p2Buf.flip();
        ballX.flip();
        ballY.flip();

        p1Buf.get(p1Byte);
        p2Buf.get(p2Byte);
        ballX.get(bxByte);
        ballY.get(byByte);

        byte[] finalByte = new byte[8];
        finalByte[0] = p1Byte[0];
        finalByte[1] = p1Byte[1];
        finalByte[2] = p2Byte[0];
        finalByte[3] = p2Byte[1];
        finalByte[4] = bxByte[0];
        finalByte[5] = bxByte[1];
        finalByte[6] = byByte[0];
        finalByte[7] = byByte[1];

        System.out.println(finalByte[6] + " | " + finalByte[7]);

        return finalByte;
    }


    @Override
    public void run() {

        // Set variables
        PlayerOnePosition = (double) PongEnv.WIN_HEIGHT/2;
        PlayerTwoPosition = (double) PongEnv.WIN_HEIGHT/2;
        p1LastUpdate = System.currentTimeMillis();
        p2LastUpdate = System.currentTimeMillis();
        ballPosition = new double[]{(double) PongEnv.WIN_WIDTH/2, (double) PongEnv.WIN_HEIGHT/2};

        // Log the activation of the tGameLoop thread
        logger.info("Game Loop Thread started successfully.");

        // Log the initial time the loop started
        long initialTime = System.nanoTime();

        // Declare the time triggers
        final double timeU = 1000000000 / UPS;
        final double timeP = 1000000000 / PPS;

        // Delta/change in time for triggers
        double deltaU = 0, deltaP = 0;

        // Information about the game loop
        int updates = 0, pushes = 0;

        // Main game processing loop
        loopRunning = true;
        while (loopRunning) {

            // Save the current time
            long currentTime = System.nanoTime();

            // Increment our delta times
            deltaU += (currentTime - initialTime) / timeU;
            deltaP += (currentTime - initialTime) / timeP;

            // Reset the initial time for the next iteration
            initialTime = currentTime;

            // deltaU(update) is greater than 1 execute that
            if (deltaU >= 1) {
                // getInput();
                // update();
                updates++;
                deltaU--;
            }

            if (deltaP >= 1) {
                // Push to clients
                pushes++;
                deltaP--;
            }
        }
    }
}
