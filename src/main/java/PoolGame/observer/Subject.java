package PoolGame.observer;

/** Interface for Subject to be observed. */
public interface Subject {

    /**
     * Attaches an observer to the subject
     * 
     * @param observer the observer
     */
    void attach(Observer observer);

    /**
     * Detaches an observer from the subject
     * 
     * @param observer the observer
     */
    void detach(Observer observer);

    /**
     * Updates the observers.
     * 
     */
    void alert();
    
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
