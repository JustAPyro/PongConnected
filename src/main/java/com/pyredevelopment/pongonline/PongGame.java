package com.pyredevelopment.pongonline;

public class PongGame {

    public final int PLAYERONE = 1;
    public final int PLAYERTWO = 2;

    private short PlayerOnePosition;
    private short PlayerTwoPosition;

    public void PongGame() {
        PlayerOnePosition = 240;
        PlayerTwoPosition = 240;
    }


    public byte[] getState() {
        return new byte[1];
    }


}
