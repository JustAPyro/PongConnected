package com.pyredevelopment.pongonline.drivers;

import com.pyredevelopment.pongonline.PongClient;
import javafx.application.Application;

public class ClientDriver {
    public static void main(String[] args) {
        Application.launch(PongClient.class, args);
    }

    public ClientDriver() {
        Application.launch(PongClient.class);
    }
}
