package PoolGame.memento;

import java.util.ArrayList;

/** Caretaker of the Memento which stores the last move made. */
public class Caretaker {
    
    // could be extended for multiple undos using list of states
    // private ArrayList<Memento> mementoList = new ArrayList<Memento>();
    private Memento state;

    /**
     * Sets the caretakers state to be a Memento instance
     * 
     * @param state the saved Memento.
     */
    public void add(Memento state) {
        // mementoList.add(state);
        this.state = state;
    }

    /**
     * Gets the last saved state of game (balls, score, time)
     * 
     * @return The saved Memento.
     */
    public Memento get() {
        return this.state;
        // return mementoList.get(mementoList.size() - 1);
    }
}