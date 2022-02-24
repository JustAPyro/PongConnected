package com.pyredevelopment.pongonline.game;

public class GameManager {

    private static GameManager gm;

    private GameManager() {

    }

    public static GameManager get() {
        if (gm == null)
            gm = new GameManager();

        return gm;
    }

}
