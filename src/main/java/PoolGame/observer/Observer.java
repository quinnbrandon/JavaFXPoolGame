package PoolGame.observer;

/** Interface for observing balls. */
public interface Observer {

    /**
     * Updates the values being observed.
     * 
     */
    void update();

    /**
     * Getter method for whether subject is active.
     * 
     * @return true if subject is active.
     */
    boolean isActive();

    /**
     * Getter method for subject score value.
     * 
     * @return subject score value.
     */
    int getScoreValue();
}