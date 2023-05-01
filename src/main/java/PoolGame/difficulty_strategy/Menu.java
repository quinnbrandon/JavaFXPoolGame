package PoolGame.difficulty_strategy;

import PoolGame.*;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

/** Has buttons for changing difficulty and calls on a strategy. */
public class Menu {
    // buttons to load game manager
    private Stage primaryStage;
    private ArrayList<Button> buttons = Config.getButtons();
    private LevelStrategy strategy;

    public Menu(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Pane pane = new Pane();
        setClickEvents(pane);
    }

    /**
     * Manages button presses.
     * 
     * @param pane
     */
    private void setClickEvents(Pane pane) {
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            String difficulty = button.getText();
            button.setOnAction(actionEvent ->  {
                loadLevel(difficulty);
            });
        }
    }

    /**
     * Loads the a given level.
     * 
     * @param difficulty the level difficulty.
     */
    public void loadLevel(String difficulty) {
        if (difficulty.equals("Easy")) {
            this.strategy = new EasyLevel();
            strategy.loadLevel(primaryStage);
        }
        else if (difficulty.equals("Normal")) {
            this.strategy = new NormalLevel();
            strategy.loadLevel(primaryStage);
        }
        else if (difficulty.equals("Hard")) {
            this.strategy = new HardLevel();
            strategy.loadLevel(primaryStage);
        }
    }
}