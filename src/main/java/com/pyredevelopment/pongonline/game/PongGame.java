package com.pyredevelopment.pongonline.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentHashMap;

import com.pyredevelopment.pongonline.PongEnv;
import com.pyredevelopment.pongonline.gameobjects.PongBall;
import com.pyredevelopment.pongonline.gameobjects.PongPaddle;
import com.pyredevelopment.pongonline.network.Server;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PongGame implements Runnable {

    // - - - - - - - - - - Static Variables - - - - - - - - - -

    // These variables determine how often certain method calls happen in game loop
    private final static double UPS = 60; // Updates per second
    private final static double PPS = 3;  // Pushes per second (Server)

    // Create a logger for this file to track its behavior.
    private final static Logger logger = LogManager.getLogger(PongGame.class);

    private PongBall ball;
    private PongPaddle[] players;

    private final double speed = .75;

    private boolean loopRunning = false;


    public PongGame() {
        // Start a new game thread
        Thread gameThread = new Thread(this, "tGame");
        gameThread.start();
    }



    // decode an incoming byte array
    public static short[] decodeState(byte[] incomingBytes) {

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

        short p1Short = (short) Math.round(players[0].getPosition());
        short p2Short = (short) Math.round(players[1].getPosition());
        short ballXShort = (short) Math.round(ball.getX());
        short ballYShort = (short) Math.round(ball.getY());


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

        return finalByte;
    }

    private PongPaddle getPlayer(String playerID) {

        if (players[0].getID() == null) {
            players[0].setID(playerID);
            return players[0];
        }
        else if (players[0].getID().equals(playerID)) {
            return players[0];
        }
        else if (players[1].getID() == null) {
            players[1].setID(playerID);
            return players[1];
        }
        else if (players[1].getID().equals(playerID)) {
            return players[1];
        }

            throw new ArithmeticException("TOO MANY PLAYERS");

    }

    private void update(ConcurrentHashMap<String, boolean[]> input) {

        for (String player : input.keySet()) {
            getPlayer(player).update(input.get(player));
        }

        ball.update();
    }

    @Override
    public void run() {

        // Create players
        players = new PongPaddle[2];
        players[0] = new PongPaddle((double) PongEnv.WIN_HEIGHT/2);
        players[1] = new PongPaddle((double) PongEnv.WIN_HEIGHT/2);

        // Set variables
        ball = new PongBall((double) PongEnv.WIN_WIDTH/2, (double) PongEnv.WIN_HEIGHT/2);

        // Create a new server
        Server networkServer = new Server(9875);
        networkServer.start();

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
                ConcurrentHashMap<String, boolean[]> input = networkServer.getAllInput();
                update(input);
                networkServer.push(getState());
                updates++;
                deltaU--;
            }

            if (deltaP >= 1) {
                //networkServer.push(getState());
                pushes++;
                deltaP--;
            }
        }
    }
}
