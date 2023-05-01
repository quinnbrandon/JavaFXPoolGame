package PoolGame;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

/** Holds static final data. */
public class Config {
    private static final double TABLEBUFFER = 50;
    private static final double TABLEEDGE = 10;
    private static final ArrayList<Button> BUTTONS = new ArrayList<>(List.of(new Button("Easy"), 
    new Button("Normal"), 
    new Button("Hard")));

    /**
     * Returns the buffer around the table.
     * 
     * @return buffer
     */
    public static double getTableBuffer() {
        return TABLEBUFFER;
    }

    /**
     * Returns the edge of the table.
     * 
     * @return edge length.
     */
    public static double getTableEdge() {
        return TABLEEDGE;
    }

    /**
     * Returns game difficulty buttons.
     * 
     * @return buttons.
     */
    public static ArrayList<Button> getButtons() {
        return BUTTONS;
    }
}
