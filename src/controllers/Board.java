package controllers;

import core.Segment;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.*;
import scenes.Game;
import scenes.ViewScene;
import tools.Log;
import views.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Board extends Observable {

    private static final int BERSERK_TIME = 10;

    public interface OnGameOver {
        void onGameOver(String message);
    }

    public Field[][] fields;

    private Game game;
    private Vector dimension;
    private List<Segment> graph = new ArrayList<>();;

    private TextView berserkView = null;

    private int total = 0;
    private int points = 0;
    private boolean berserk = false;

    public PacMan pacman = new PacMan(new Vector(0, 14));
    public List<Phantom> phantoms = new ArrayList<>();

    {
        phantoms.add(new Phantom(new Vector(12, 14)));
        phantoms.add(new Phantom(new Vector(13, 14)));
        phantoms.add(new Phantom(new Vector(14, 14)));
        phantoms.add(new Phantom(new Vector(15, 14)));
    }

    private OnGameOver onGameOver;

    public Board(Game game) {
        this.onGameOver = game;
        this.game = game;

        createBoard("./res/maps/map_1.txt");
        resolveGraph(pacman.getVector(), Segment.HORIZONTAL);
    }

    public Vector getDimension() {
        return dimension;
    }

    public void onUpdateKeyListener(KeyCode key) {
        pacman.updatePosition(this, key);

        resolveGameState();
    }

    public void onUpdatePhantoms() {
        for(Phantom phantom : phantoms)
            phantom.updateSpirit(this);

        resolveGameState();
    }

    public void onUpdatePacMan() {
        pacman.update(null);
    }

    public void createCharacters(Pane pane) {
        pacman.render(pane);

        for(Phantom phantom : phantoms)
            phantom.render(pane);

//        phantoms.get(0).findPath(this, pacman);
    }

    public Field getField(Vector vector) {
        int x = vector.getX() < 0 ? dimension.getX() + vector.getX() : vector.getX() % dimension.getX();
        int y = vector.getY() < 0 ? dimension.getY() + vector.getY() : vector.getY() % dimension.getY();

        return fields[x][y];
    }

    private void resolveScore(int value) {
        if(value >= 0) {
            points += value;
            total--;

            setChanged();
            notifyObservers(String.valueOf(points));
        }
    }

    public void checkCollisionPhantom(Phantom phantom) {
        if(phantom.getVector().equals(pacman.getVector())) {
            if(berserk) {
                phantom.disable();
                resolveScore(Phantom.PHANTOM_SCORE);
            } else {
                onGameOver.onGameOver("Defeat, try again! :)");
            }
        }
    }

    public void checkCollisionPhantoms() {
        for(Phantom phantom : phantoms) {
            checkCollisionPhantom(phantom);
        }
    }

    public void addBerserkView(TextView berserkView) {
        this.berserkView = berserkView;
    }

    public void resolveGameState() {
        if(total == 0 || Phantom.resolvePhantoms(phantoms)) {
            onGameOver.onGameOver("You won, Congrats");
        }
    }

    public void checkCollisionPoints(Vector vector) {
        Field field = getField(vector);
        if(field instanceof Point) {
            Point point = (Point) field;
            int value = point.getValue();

            if(point.powerPoint) {
                Phantom.resolveBerserkState(phantoms, true);
                berserkView.setFill(Color.RED);
                berserk = true;

                game.registerTimeOutListener(() -> {
                    Phantom.resolveBerserkState(phantoms, false);
                    berserkView.setFill(Color.WHITE);
                    berserk = false;
                }, BERSERK_TIME);
            }

            point.disable();

            resolveScore(value);
        }
    }

    public Vector resolveBoundaries(Vector vector) {
        int x = vector.getX() < 0 ? dimension.getX() - 1 : vector.getX() % dimension.getX();
        int y = vector.getY() < 0 ? dimension.getY() - 1: vector.getY() % dimension.getY();

        return new Vector(x, y);
    }

    public PacMan getPacman() {
        return pacman;
    }

    public Field[][] getFields() {
        return fields;
    }

    public List<Phantom> getPhantoms() {
        return phantoms;
    }

    public List<Segment> getGraph() {
        return graph;
    }

    public void resolveGraph(Vector start, int orientation) {
        Segment.resolvePosition(graph, this, start, orientation);
    }

    public void createBoard(String path) {
        File file = new File(path);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            if(line == null)
                return;

            String[] dimensions = line.split(" ");
            int width = Integer.valueOf(dimensions[1]);
            int height = Integer.valueOf(dimensions[0]);

            dimension = new Vector(width, height);
            fields = new Field[width][height];

            int row = 0;
            while((line = bufferedReader.readLine()) != null) {
                for(int col = 0; col < line.length(); col++) {
                    char type = line.charAt(col);

                    Vector vector = new Vector(row, col);

                    switch(type) {
                        case 'm' : {
                            fields[row][col] = new Location(vector, 'm');
                            break;
                        }
                        case 'n' : {}
                        case 'o' : {}
                        case 'p' : {
                            total++;

                            fields[row][col] = new Point(vector, type);
                            break;
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
