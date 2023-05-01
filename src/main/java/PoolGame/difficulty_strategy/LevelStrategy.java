package PoolGame.difficulty_strategy;

import javafx.stage.Stage;

/** Interface for loading levels. */
public interface LevelStrategy {

    /**
     * Loads a difficulty level.
     * 
     * @param primaryStage
     */
    public void loadLevel(Stage primaryStage);
}