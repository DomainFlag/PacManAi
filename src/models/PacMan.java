package models;

import controllers.Board;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class PacMan extends Spirit {

    private static final List<String> paths = new ArrayList<>();

    static {
        paths.add("file:./../res/textures/pacman-hungry.png");
        paths.add("file:./../res/textures/pacman-fed.png");
    }

    private static final List<Image> images = new ArrayList<>();
    private int imagePosition = 0;

    static {
        for(String path : paths) {
            images.add(new Image(path));
        }
    }

    public PacMan(Vector vector) {
        super(vector, images);
    }

    public void updatePosition(Board board, KeyCode keyCode) {
        int pos;
        switch(keyCode) {
            case UP : {
                getImageView().setRotate(270);
                pos = 1;
                break;
            }
            case DOWN : {
                getImageView().setRotate(90);
                pos = 0;
                break;
            }
            case RIGHT : {
                getImageView().setRotate(0);
                pos = 2;
                break;
            }
            default : {
                getImageView().setRotate(180);
                pos = 3;
            }
        }

        Vector position = getVector().add(Vector.getDirection(pos));
        if(!(board.getField(position) instanceof Wall)) {
            Vector currentPosition = board.resolveBoundaries(position);

            board.checkCollisionPhantoms();
            board.checkCollisionPoints(currentPosition);

            setVector(currentPosition);
            updateLayout();
        }
    }

    @Override
    public void update(Board board) {
        imagePosition = (imagePosition + 1) % images.size();

        getImageView().setImage(images.get(imagePosition));
    }
}
