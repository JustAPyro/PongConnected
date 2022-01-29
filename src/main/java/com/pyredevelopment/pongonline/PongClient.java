package com.pyredevelopment.pongonline;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PongClient extends Application {

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


        stage.setScene(scene);
        stage.show();
    }

}
