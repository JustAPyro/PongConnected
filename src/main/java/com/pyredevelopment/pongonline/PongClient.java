package com.pyredevelopment.pongonline;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;

public class PongClient extends Application {

    private int PLAYER = 1;

    private DatagramSocket socket;
    private InetAddress address;
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
                            = new DatagramPacket(buffer, 1, InetAddress.getLocalHost(), 4445);
                    socket.send(packet);
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
