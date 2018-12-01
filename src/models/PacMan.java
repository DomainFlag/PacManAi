package models;

import controllers.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import tools.Log;

import java.util.ArrayList;
import java.util.List;

public class PacMan  {

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

    private Vector vector;
    private ImageView imageView;

    public PacMan(Vector vector) {
        this.vector = vector;
    }

    public void render(Pane pane) {
        imageView = new ImageView(images.get(imagePosition));
        imageView.setFitWidth(8);
        imageView.setFitHeight(8);
        setLayout(imageView, vector);

        pane.getChildren().add(imageView);
    }

    public void setLayout(ImageView imageView, Vector vector) {
        imageView.setLayoutX(vector.getX() * 8);
        imageView.setLayoutY(vector.getY() * 8);
    }

    public void updateWobble() {
        imagePosition = (imagePosition + 1) % images.size();
        imageView.setImage(images.get(imagePosition));
    }

    public Vector getVector() {
        return vector;
    }

    public void update(Board board, KeyCode keyCode) {
        int pos;
        switch(keyCode) {
            case UP : {
                imageView.setRotate(270);
                pos = 1;
                break;
            }
            case DOWN : {
                imageView.setRotate(90);
                pos = 0;
                break;
            }
            case RIGHT : {
                imageView.setRotate(0);
                pos = 2;
                break;
            }
            default : {
                imageView.setRotate(180);
                pos = 3;
            }
        }

        Vector position = vector.add(Vector.getDirection(pos));
        if(board.checkBoundaries(position) && !(board.getField(position) instanceof Wall)) {
            vector = position;

            setLayout(imageView, vector);

            board.checkCollisionPhantoms(vector);
            board.checkCollisionPoints(vector);
        }
    }
}
