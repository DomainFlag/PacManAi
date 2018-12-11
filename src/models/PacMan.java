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
    private Vector direction = new Vector(1, 0);
    private int imagePosition = 0;

    static {
        for(String path : paths) {
            images.add(new Image(path));
        }
    }

    public PacMan(Vector vector) {
        super(vector, images);
    }

    public void updatePosition(KeyCode keyCode) {
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

        direction = Vector.getDirection(pos);
    }

    @Override
    public void update(Board board) {
        Vector position = getVector().add(direction);
        if(!(board.getField(position) instanceof Wall)) {
            Vector currentPosition = board.resolveBoundaries(position);

            board.checkCollisionPhantoms();
            board.checkCollisionPoints(currentPosition);

            setVector(currentPosition);
            updateLayout();
        }


        imagePosition = (imagePosition + 1) % images.size();

        getImageView().setImage(images.get(imagePosition));
    }
}
