package com.pyredevelopment.pongonline;

public class PongPlayer {

    private String playerID;
    private double position;

    public PongPlayer(double position) {
        this.position = position;
    }

    public void setID(String playerID) {
        this.playerID = playerID;
    }

    public String getID() {
        return playerID;
    }

    public void update(boolean[] inputs) {

    }

}
