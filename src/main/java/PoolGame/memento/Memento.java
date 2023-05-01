package PoolGame.memento;

import java.util.ArrayList;
import PoolGame.objects.Ball;

/** The saved game state (balls, score, time). */
public class Memento {
    
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private int score = 0;
    private double gameTime;

    public Memento(ArrayList<Ball> balls, int score, double gameTime){
        this.balls = balls;
        this.score = score;
        this.gameTime = gameTime;
    }

    /**
     * Gets the balls of the last saved state
     * 
     * @return The list of balls.
     */
    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    /**
     * Gets the score of the last saved state
     * 
     * @return The score of the game.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Gets the time of the last saved state
     * 
     * @return The time of the game.
     */
    public double getTime() {
        return this.gameTime;
    }
}
