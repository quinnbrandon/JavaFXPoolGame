package PoolGame.memento;

import java.util.ArrayList;
import PoolGame.objects.Ball;

/** Creates the Memento. */
public class Originator {

    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private int score = 0;
    private double gameTime;

    /**
     * Sets the balls
     * 
     * @param balls balls of the game.
     */
    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }

    /**
     * Gets the balls
     * 
     * @return The list of balls.
     */
    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    /**
     * Sets the score
     * 
     * @param score score of the game.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the score
     * 
     * @return The score of the game.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the time
     * 
     * @param gameTime time of the game.
     */
    public void setTime(double gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * Gets the time
     * 
     * @return The time of the game.
     */
    public double getTime() {
        return this.gameTime;
    }

    /**
     * Saves the state of game (balls, score, time) into a Memento
     * 
     * @return The saved Memento.
     */
    public Memento saveStateToMemento() {
        return new Memento(balls, score, gameTime);
    }

    /**
     * Gets the state of game (balls, score, time) from a Memento
     * 
     * @param memento the memento of the last saved state
     */
    public void getStateFromMemento(Memento memento) {
        this.balls = memento.getBalls();
        this.score = memento.getScore();
        this.gameTime = memento.getTime();
    }
}