package com.pyredevelopment.pongonline.game;

public class PongPlayer {

    private String playerID;
    private double position;
    private final double speed = 3;

    public PongPlayer(double position) {
        this.position = position;
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
