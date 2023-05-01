package PoolGame;

import PoolGame.config.*;
import PoolGame.difficulty_strategy.*;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

/** Main application entry point. */
public class App extends Application {

    /**
     * @param args First argument is the path to the config file
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    /**
     * Starts the application.
     * 
     * @param primaryStage The primary stage for the application.
     */
    public void start(Stage primaryStage) {
        // Strategy to load levels and change level difficulty, initially config_easy
        Menu menu = new Menu(primaryStage);
        menu.loadLevel("Easy");
    }

    /**
     * Checks if the config file path is given as an argument.
     * Set the easy level as the default level displayed.
     * @param args
     * @return config path.
     */
    private static String checkConfig(List<String> args) {
        String configPath;
        if (args.size() > 0) {
            configPath = args.get(0);
        } else {
            // set the easy level as the default level displayed
            configPath = "src/main/resources/config_easy.json";
        }
        return configPath;
    }
}
