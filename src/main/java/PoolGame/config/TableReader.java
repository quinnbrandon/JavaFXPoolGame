package PoolGame.config;

import PoolGame.GameManager;
import PoolGame.objects.*;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** Readers table section of JSON. */
public class TableReader implements Reader {
    /**
     * Parses the JSON file and builds the table.
     * 
     * @param path        The path to the JSON file.
     * @param gameManager The game manager.
     */
    public void parse(String path, GameManager gameManager) {
        JSONParser parser = new JSONParser();
		ArrayList<Pocket> pockets = new ArrayList<Pocket>();

        try {
            Object object = parser.parse(new FileReader(path));

            // convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // reading the Table section:
            JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

            // reading a value from the table section
            String tableColour = (String) jsonTable.get("colour");

            // reading a coordinate from the nested section within the table
            // note that the table x and y are of type Long (i.e. they are integers)
            Long tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
            Long tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

            // getting the friction level.
            // This is a double which should affect the rate at which the balls slow down
            Double tableFriction = (Double) jsonTable.get("friction");

            // Check friction level is between 0 and 1
            if (tableFriction >= 1 || tableFriction <= 0) {
                System.out.println("Friction must be between 0 and 1");
                System.exit(0);
            }

            // reading the "Table: pockets" array:
			JSONArray jsonPockets = (JSONArray) jsonTable.get("pockets");

			// reading from the array:
			for (Object obj : jsonPockets) {
				JSONObject jsonPocket = (JSONObject) obj;

				// the pocket position and mass are doubles
				Double pocketPositionX = (Double) ((JSONObject) jsonPocket.get("position")).get("x");
				Double pocketPositionY = (Double) ((JSONObject) jsonPocket.get("position")).get("y");

				// Check pocket is within bounds
				if (pocketPositionX > tableX || pocketPositionY > tableY) {
					System.out.println("Pocket position is outside the table");
					System.exit(0);
				}

				Double radius = (Double) jsonPocket.get("radius");

                Pocket pocket = new Pocket(pocketPositionX, pocketPositionY, radius);
                pockets.add(pocket);
            }

            gameManager.setTable(new Table(tableColour, tableX, tableY, tableFriction, pockets));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
