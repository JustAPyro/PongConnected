package com.pyredevelopment.pongonline;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;

import static com.pyredevelopment.pongonline.PongEnv.*;

public class PongClient extends Application {

    private final int PLAYER = 0;
    private final String IP_ADDRESS = "98.118.61.212";
    private final int PORT_NUMBER = 9875;

    private DatagramPacket incomingPacket;
    private DatagramSocket socket;
    private final HashSet<KeyCode> keysPressed = new HashSet<>();


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage)  {

        stage.setTitle("Pong Connected Client");

        VBox box = new VBox();
        Canvas canvas = new Canvas(PongEnv.WIN_WIDTH, PongEnv.WIN_HEIGHT);
        box.getChildren().add(canvas);


        Scene scene = new Scene(box);
        scene.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        StringBuilder binaryByte = new StringBuilder();
        AnimationTimer clock = new AnimationTimer() {
            @Override
            public void handle(long l) {

                binaryByte.setLength(0);
                binaryByte.append("+");
                binaryByte.append(PLAYER);
                if (keysPressed.contains(KeyCode.W)) binaryByte.append("1"); else binaryByte.append("0");
                if (keysPressed.contains(KeyCode.S)) binaryByte.append("1"); else binaryByte.append("0");
                binaryByte.append("0000");

                byte[] buffer = {Byte.parseByte(binaryByte.toString(), 2)};

                try {

                    DatagramPacket packet
                            = new DatagramPacket(buffer, 1, InetAddress.getLocalHost(), PORT_NUMBER);
                    socket.send(packet);

                    byte[] incomingBuffer = new byte[8];
                    incomingPacket = new DatagramPacket(incomingBuffer, 4);
                    socket.receive(incomingPacket);

                    short[] positions = PongGame.decodeState(incomingBuffer);

                    GraphicsContext gc = canvas.getGraphicsContext2D();

                    // Clear the canvas
                    gc.clearRect(0,  0, canvas.getWidth(), canvas.getHeight());

                    // Set the background color then fill the background
                    gc.setFill(BACKGROUND_COLOR);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    // Set paddle color and draw them
                    gc.setFill(PADDLE_COLOR);
                    drawPaddle(gc, 0 + PADDLE_PADDING, positions[0]);
                    drawPaddle(gc, (int) (canvas.getWidth()-PADDLE_PADDING), positions[1]);
                    drawBall(gc, positions[2], positions[3]);

                    System.out.println("P1: " + positions[0] + "P2: " + positions[1]);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clock.start();

        stage.setScene(scene);
        stage.show();
    }

    private void drawBall(GraphicsContext gc, int x, int y) {
        gc.fillOval(x-(double) BALL_SIZE/2, y-(double) BALL_SIZE/2, BALL_SIZE, BALL_SIZE);
    }

    private void drawPaddle(GraphicsContext gc, int x, int y) {

        // Draw the rectangle for the paddle
        gc.fillRect(x - PADDLE_WIDTH/2, y - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

    }

}
