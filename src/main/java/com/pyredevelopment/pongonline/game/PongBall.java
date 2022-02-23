package com.pyredevelopment.pongonline.game;

import com.pyredevelopment.pongonline.PongEnv;

/**
 * This class represents the ball in the game of Pong.
 *
 * @author Luke hanna
 * @version 1.0.0 (Last Updated 2/23/2022)
 */
public class PongBall {

    // First Order Movement
    private double x;   // Horizontal position of the ball
    private double y;   // Vertical position of the ball

    // Second Order Movement
    private double speed = 3;
    private double direction = Math.PI/1.6;

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

    public void setDirection(double direction) {
        this.direction = Math.toRadians(direction);
    }

    public double getDirection() {
        return Math.toDegrees(direction);
    }

    public void update() {

        double px = x + speed * Math.cos(direction - Math.PI/2);
        double py = y + speed * Math.sin(direction - Math.PI/2);

        if (px >= PongEnv.WIN_WIDTH) {
            // If we've hit the right wall
            setDirection(360-getDirection());
        }
        else if (px <= 0) {
            setDirection(360-getDirection());

        }
        else if (py <= 0) {
            setDirection(180-getDirection());
        }
        else if (py >= PongEnv.WIN_HEIGHT) {
            setDirection(180-getDirection());
        }

        x =x + speed * Math.cos(direction - Math.PI/2);
        y =y + speed * Math.sin(direction - Math.PI/2);


    }

}