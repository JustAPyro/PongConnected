package com.pyredevelopment.pongonline.game;

/**
 * This class represents the ball in the game of Pong.
 *
 * @author Luke hanna
 * @version 1.0.0 (Last Updated 2/23/2022)
 */
public class PongBall {

    private double x;   // Horizontal position of the ball
    private double y;   // Vertical position of the ball

    /**
     * Creates a new PongBall with the starting location (x, y) given in pixel locations.
     * @param x The horizontal starting position of the PongBall.
     * @param y The vertical starting position of the PongBall.
     */
    public PongBall(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the horizontal (x) position of the PongBall as a double.
     * @return The horizontal pixel position of the PongBall.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the vertical (y) position of the PongBall as a double.
     * @return The vertical pixel position of the PongBall.
     */
    public double getY() {
        return y;
    }

}