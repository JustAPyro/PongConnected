package com.pyredevelopment.pongonline.gameobjects;

import com.pyredevelopment.pongonline.PongEnv;
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
        boolean sPress = inputs[1];
        boolean wPress = inputs[2];

        if (wPress ^ sPress) {
            if (wPress && position - (double)PongEnv.PADDLE_HEIGHT/2 > 0)
                position -= speed;
            if (sPress && position + (double)PongEnv.PADDLE_HEIGHT/2 < PongEnv.WIN_HEIGHT)
                position += speed;
        }
    }

}
