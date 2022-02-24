package com.pyredevelopment.pongonline.gameobjects;

import com.pyredevelopment.pongonline.PongEnv;
import com.pyredevelopment.pongonline.game.GameManager;

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
    public PongBall(double x, double y) {

        // Initialize the starting values
        this.x = x;
        this.y = y;

        // Register the ball with the GameManager
        GameManager.get().register(this);

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

    /**
     * Allows you to set the direction the ball is moving in degrees. 0 is directly north/up, 90 is right, and so on.
     * @param direction The direction the ball is moving in degrees.
     */
    public void setDirection(double direction) {
        this.direction = Math.toRadians(direction);
    }

    /**
     * Returns the direction the ball is moving in degrees, with 0 being directly up, and 90 being to the right.
     * @return The direction of the ball in degrees.
     */
    public double getDirection() {
        return Math.toDegrees(direction);
    }

    /**
     * Updates the position of the ball based on the PongBalls current variables.
     */
    public void update() {

        // Predict the new position
        double px = x + speed * Math.cos(direction - Math.PI/2);
        double py = y + speed * Math.sin(direction - Math.PI/2);

        if (px >= PongEnv.WIN_WIDTH) {
            // If we've hit the right wall
            setDirection(360-getDirection());
        }
        else if (px <= 0) {
            // If we've hit the left wall
            setDirection(360-getDirection());
        }
        else if (py <= 0) {
            // If we hit the top
            setDirection(180-getDirection());
        }
        else if (py >= PongEnv.WIN_HEIGHT) {
            // If we hit the bottom
            setDirection(180-getDirection());
        }
        // Check for collisions with left paddle
        else if ((px <= PongEnv.PADDLE_PADDING + ((double) PongEnv.PADDLE_WIDTH/2)) &&
                py >= GameManager.get().getPaddlePos(1) - (double) PongEnv.PADDLE_HEIGHT/2 &&
                py <= GameManager.get().getPaddlePos(1) + (double) PongEnv.PADDLE_HEIGHT/2) {
            setDirection(360 - getDirection());
        }
        // Check for collisions with right paddle
        else if ((px <= PongEnv.PADDLE_PADDING + ((double) PongEnv.PADDLE_WIDTH/2)) &&
                py >= GameManager.get().getPaddlePos(2) - (double) PongEnv.PADDLE_HEIGHT/2 &&
                py <= GameManager.get().getPaddlePos(2) + (double) PongEnv.PADDLE_HEIGHT/2) {
            setDirection(360 - getDirection());
        }

        // Update the actual position.
        x += speed * Math.cos(direction - Math.PI/2);
        y += speed * Math.sin(direction - Math.PI/2);

    }

}