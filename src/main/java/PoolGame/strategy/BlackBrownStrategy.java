package PoolGame.strategy;

public class BlackBrownStrategy extends PocketStrategy {

    /** Creates a new Black or Brown ball strategy. */
    public BlackBrownStrategy() {
        this.lives = 3;
    }

    public void reset() {
        this.lives = 3;
    }
}