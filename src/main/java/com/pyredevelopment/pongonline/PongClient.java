package com.pyredevelopment.pongonline;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;

public class PongClient extends Application {

    private final int PLAYER = 0;
    private final String IP_ADDRESS = "98.118.61.212";
    private final int PORT_NUMBER = 9875;

    private DatagramSocket socket;
    private final HashSet<KeyCode> keysPressed = new HashSet<>();


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage)  {

        stage.setTitle("Pong Connected Client");

        VBox box = new VBox();
        Canvas canvas = new Canvas(640, 480);
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

                    byte[] incomingBuffer = new byte[4];
                    DatagramPacket incomingPacket = new DatagramPacket(incomingBuffer, 4);
                    socket.receive(incomingPacket);

                    short[] positions = PongGame.decodeState(incomingBuffer);

                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.clearRect(0,  0, canvas.getWidth(), canvas.getHeight());


                    gc.fillRect(25, positions[0], 20, 50);
                    gc.fillRect(300, positions[1], 20, 50);



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

}
