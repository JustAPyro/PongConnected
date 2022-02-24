package com.pyredevelopment.pongonline.gameobjects;

import com.pyredevelopment.pongonline.game.GameManager;

public class PongPaddle {

    private String playerID;
    private double position;
    private final double speed = 3;

    public PongPaddle(double position) {

        // Save the starting position
        this.position = position;

        // Register this paddle with the game manager
        GameManager.get().register(this);
    }

    public void setID(String playerID) {
        this.playerID = playerID;
    }

    public String getID() {
        return playerID;
    }

    public double getPosition() {
        return position;
    }

    public void update(boolean[] inputs) {
        boolean wPress = inputs[1];
        boolean sPress = inputs[2];

        if (wPress ^ sPress) {
            if (wPress)
                position += speed;
            if (sPress)
                position -= speed;
        }
    }

}
