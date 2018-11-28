package models;

import controllers.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import tools.Log;

public class PacMan  {

    private static final String path = "file:./../res/textures/pacman.png";
    private static final Image image = new Image(path);

    private Vector vector;
    private ImageView imageView;

    public PacMan(Vector vector) {
        this.vector = vector;
    }

    public void render(Pane pane) {
        imageView = new ImageView(image);
        imageView.setFitWidth(8);
        imageView.setFitHeight(8);
        setLayout(imageView, vector);

        pane.getChildren().add(imageView);
    }

    public void setLayout(ImageView imageView, Vector vector) {
        imageView.setLayoutX(vector.getX() * 8);
        imageView.setLayoutY(vector.getY() * 8);
    }

    public void update(Board board, KeyCode keyCode) {
        int pos;
        switch(keyCode) {
            case UP : {
                pos = 1;
                break;
            }
            case DOWN : {
                pos = 0;
                break;
            }
            case RIGHT : {
                pos = 2;
                break;
            }
            default : pos = 3;
        }

        Vector position = vector.add(Vector.getDirection(pos));
        if(board.checkBoundaries(position) && board.getField(position) instanceof Location) {
            vector = position;

            setLayout(imageView, vector);
        }
    }
}
