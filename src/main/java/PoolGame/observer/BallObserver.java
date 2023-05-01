package PoolGame.observer;

/** ConcreteObserver of a Ball. */
public class BallObserver implements Observer {
    
    private final Subject subject;
    private boolean isActive;
    private int scoreValue;

    public BallObserver(Subject subject) {
        this.subject = subject;
        this.scoreValue = subject.getScoreValue();
        this.isActive = subject.isActive();
    }

    /**
     * Updates the values being observed.
     * 
     */
    @Override
    public void update() {
        this.scoreValue = subject.getScoreValue();
        this.isActive = subject.isActive();
    }

    /**
     * Getter method for whether ball is active.
     * 
     * @return true if ball is active.
     */
    @Override
    public boolean isActive() {
        return this.isActive;
    }

    /**
     * Getter method for ball score value.
     * 
     * @return ball score value.
     */
    @Override
    public int getScoreValue() { 
        return this.scoreValue; 
    }
}