package com.pyredevelopment.pongonline;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;

public class PongClient extends Application {

    private HashSet<KeyCode> keysPressed = new HashSet<>();

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
        scene.setOnKeyPressed(e -> {
           keysPressed.add(e.getCode());
        });
        scene.setOnKeyReleased(e -> {
            keysPressed.remove(e.getCode());
        });

        stage.setScene(scene);
        stage.show();
    }

}
