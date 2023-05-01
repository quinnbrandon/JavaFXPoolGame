package PoolGame.objects;

import PoolGame.strategy.PocketStrategy;
import PoolGame.strategy.BallStrategy;
import PoolGame.strategy.BlackBrownStrategy;
import PoolGame.strategy.BlueStrategy;

/** Builds pool balls. */
public class PoolBallBuilder implements BallBuilder {
    // Required Parameters
    private String colour;
    private double xPosition;
    private double yPosition;
    private double xVelocity;
    private double yVelocity;
    private double mass;

    // Variable Parameters
    private boolean isCue = false;
    public PocketStrategy strategy;
    public int scoreValue;

    @Override
    public void setColour(String colour) {
        this.colour = colour;
    };

    @Override
    public void setxPos(double xPosition) {
        this.xPosition = xPosition;
    };

    @Override
    public void setyPos(double yPosition) {
        this.yPosition = yPosition;
    };

    @Override
    public void setxVel(double xVelocity) {
        this.xVelocity = xVelocity;
    };

    @Override
    public void setyVel(double yVelocity) {
        this.yVelocity = yVelocity;
    };

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    };

    /**
     * Builds the ball.
     * 
     * @return ball
     */
    public Ball build() {

        if (colour.equals("white")) {
            isCue = true;
            strategy = new BallStrategy();
            scoreValue = 0;
        } else if (colour.equals("blue")) {
            strategy = new BlueStrategy();
            scoreValue = 5;
        } else if (colour.equals("green")) {
            strategy = new BlueStrategy();
            scoreValue = 3;
        } else if (colour.equals("purple")) {
            strategy = new BlueStrategy();
            scoreValue = 6;
        } else if (colour.equals("black")) {
            strategy = new BlackBrownStrategy();
            scoreValue = 7;
        } else if (colour.equals("brown")) {
            strategy = new BlackBrownStrategy();
            scoreValue = 4;
        } else if (colour.equals("red")) {
            strategy = new BallStrategy();
            scoreValue = 1;
        } else if (colour.equals("orange")) {
            strategy = new BallStrategy();
            scoreValue = 8;
        } else if (colour.equals("yellow")) {
            strategy = new BallStrategy();
            scoreValue = 2;
        }

        return new Ball(colour, xPosition, yPosition, xVelocity, yVelocity, mass, isCue, strategy, scoreValue);
    }
}
