package com.pyredevelopment.pongonline.game;

import com.pyredevelopment.pongonline.gameobjects.PongBall;
import com.pyredevelopment.pongonline.gameobjects.PongPaddle;

public class GameManager {

    // Singleton instance
    private static GameManager gm;

    // - - - - - - - - - - Instance variables - - - - - - - - - -
    // These are all objects that are managed by GameManager

    private PongPaddle player1;
    private PongPaddle player2;
    private PongBall ball;

    private GameManager() {

    }

    public static GameManager get() {
        if (gm == null)
            gm = new GameManager();

        return gm;
    }

    public double getPaddlePos(int player) {
        if (player == 1)
            return player1.getPosition();
        if (player == 2)
            return player2.getPosition();

        return 0;
    }


    /**
     * Registers a paddle with the GameManager
     * @param paddle The paddle being used in the game.
     */
    public void register(PongPaddle paddle) {
        if (player1 == null)
            player1 = paddle;
        else if (player2 == null)
            player2 = paddle;
    }

    /**
     * Registers the game ball that is in the game
     * @param ball The game/pong ball.
     */
    public void register(PongBall ball) {
        this.ball = ball;
    }

}
