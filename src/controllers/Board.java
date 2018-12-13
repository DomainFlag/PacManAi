package controllers;

import models.Segment;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.*;
import scenes.Game;
import views.FieldView;
import views.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class Board extends Observable {

    // 10 seconds
    private static final long BERSERK_TIME = 10000000000L;

    public interface OnGameOver {
        void onGameOver(String message);
    }

    private Game game;
    private Playground playground;
    private HashMap<String, Segment> graph = new HashMap<>();

    private TextView berserkView = null;

    private int points = 0;
    private boolean berserk = false;

    private List<Phantom> phantoms = new ArrayList<>();
    public PacMan pacman = new PacMan(new Vector(0, 14));

    {
        phantoms.add(new Phantom(new Vector(12, 14)));
        phantoms.add(new Phantom(new Vector(13, 14)));
        phantoms.add(new Phantom(new Vector(14, 14)));
        phantoms.add(new Phantom(new Vector(15, 14)));
    }

    private OnGameOver onGameOver;

    public Board(Game game, Playground playground) {
        this.playground = playground;
        this.onGameOver = game;
        this.game = game;

        resolveGraph(pacman.getVector(), Segment.HORIZONTAL);
    }

    public Playground getPlayground() {
        return playground;
    }

    public void onUpdateKeyListener(KeyCode key) {
        Direction direction;
        switch(key) {
            case UP : {
                direction = Vector.getDirection(1);
                break;
            }
            case DOWN : {
                direction = Vector.getDirection(0);
                break;
            }
            case RIGHT : {
                direction = Vector.getDirection(2);
                break;
            }
            default : {
                direction = Vector.getDirection(3);
            }
        }

        pacman.updatePosition(direction);

        resolveGameState();
    }

    public void onUpdatePhantoms() {
        for(Phantom phantom : phantoms) {
            phantom.updateSpirit(this);
            phantom.findShortestPath(graph, pacman.getVector());
        }

        resolveGameState();
    }

    public void onUpdatePacMan() {
        pacman.update(this);
    }

    private void createCharacter(Spirit spirit, Pane pane) {
        FieldView fieldView = new FieldView();
        fieldView.inflateImage(spirit.getDefaultImage());
        fieldView.changeLayout(spirit.getVector());
        fieldView.inflate(pane);

        spirit.addObserver(fieldView);
    }

    public void createCharacters(Pane pane) {
        createCharacter(pacman, pane);

        for(Phantom phantom : phantoms) {
            createCharacter(phantom, pane);
        }
    }

    private void resolveScore(int value) {
        if(value >= 0) {
            points += value;
            playground.setTotal(playground.getTotal() - 1);

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
        if(playground.getTotal() == 0 || Phantom.resolvePhantoms(phantoms)) {
            onGameOver.onGameOver("You won, Congrats");
        }
    }

    public void checkCollisionPoints(Vector vector) {
        Field field = playground.getField(vector);
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

    public void resolveGraph(Vector start, int orientation) {
        Segment parent = Segment.resolvePosition(graph, playground, start, orientation);
    }
}
