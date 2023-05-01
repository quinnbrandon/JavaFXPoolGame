package PoolGame.difficulty_strategy;

import PoolGame.GameManager;
import PoolGame.config.*;
import javafx.stage.Stage;

/** Loads the hard level. */
public class HardLevel implements LevelStrategy {

    /**
     * Loads the hard level on the stage.
     * 
     * @param primaryStage the game stage.
     */
    @Override
    public void loadLevel(Stage primaryStage) {

        GameManager gameManager = new GameManager();

        String configPath = "src/main/resources/config_hard.json";

        ReaderFactory tableFactory = new TableReaderFactory();
        Reader tableReader = tableFactory.buildReader();
        tableReader.parse(configPath, gameManager);

        ReaderFactory ballFactory = new BallReaderFactory();
        Reader ballReader = ballFactory.buildReader();
        ballReader.parse(configPath, gameManager);

        gameManager.buildManager();

        gameManager.run();
        primaryStage.setTitle("Pool");
        primaryStage.setScene(gameManager.getScene());
        primaryStage.show();
        gameManager.run();
    }
}