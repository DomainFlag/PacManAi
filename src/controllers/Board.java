package controllers;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.*;
import tools.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public Field[][] fields;

    private Vector dimension;

    public static final PacMan pacman = new PacMan(new Vector(2, 6));
    public static final List<Phantom> phantoms = new ArrayList<>();

    static {
        phantoms.add(new Phantom(new Vector(15, 6)));
        phantoms.add(new Phantom(new Vector(19, 6)));
    }

    public Board(int width, int height) {
        dimension = new Vector(width, height);
        fields = new Field[width][height];
        createBoard("./res/maps/map.txt");
    }

    public void onUpdateKeyListener(KeyCode key) {
        pacman.update(this, key);
    }

    public void onUpdatePhantoms() {
        for(Phantom phantom : phantoms)
            phantom.update(this);
    }

    public void createCharacters(Pane pane) {
        pacman.render(pane);

        for(Phantom phantom : phantoms)
            phantom.render(pane);
    }

    public Field getField(Vector vector) {
        return fields[vector.getX()][vector.getY()];
    }

    public boolean checkBoundaries(Vector vector) {
        return vector.getX() >= 0 && vector.getX() < dimension.getX() &&
                vector.getY() >= 0 && vector.getY() < dimension.getY();
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

                    Vector vector = new Vector(col, row);

                    switch(type) {
                        case 'm' : {
                            fields[col][row] = new Location(vector, 'm');
                            break;
                        }
                        case 'o' : {
                            fields[col][row] = new Location(vector, 'o');
                            break;
                        }
                        default : {
                            fields[col][row] = new Wall(vector, type);
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
