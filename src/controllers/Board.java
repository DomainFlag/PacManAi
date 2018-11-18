package controllers;

import javafx.scene.layout.Pane;
import models.*;

import java.io.*;

public class Board {

    private static final int TEXTURE_IDENTIFIER = 0;

    public Field[][] fields;

    public Board(int width, int height) {
        fields = new Field[width][height];

        createBoard("./res/maps/map.txt");
    }

    public void createBoard(String path) {
        File file = new File(path);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            int row = 0;
            while((line = bufferedReader.readLine()) != null) {
                for(int col = 0; col < line.length(); col++) {
                    char type = line.charAt(col);

                    Vector vector = new Vector(row, col);

                    switch(type) {
                        case Location.LOC_TYPE : {
                            fields[row][col] = new Location(vector, 'p');
                        }
                        case PacMan.LOC_TYPE : {
                            fields[row][col] = new PacMan(vector, 'q');
                        }
                        case Phantom.LOC_TYPE : {
                            fields[row][col] = new Phantom(vector, 'r');
                        }
                        default : {
                            fields[row][col] = new Wall(vector, type);
                        }
                    }
                }

                row++;
            }

        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
