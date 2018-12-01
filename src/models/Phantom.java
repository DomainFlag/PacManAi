package models;

import controllers.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import tools.Log;

import java.util.Random;

public class Phantom {

    private static final String path = "file:./../res/textures/ghost.png";
    private static final Image image = new Image(path);

    private Vector vector;

    private ImageView imageView;

    public Phantom(Vector vector) {
        this.vector = vector;
    }

    public void render(Pane pane) {
        imageView = new ImageView(image);
        imageView.setFitHeight(8);
        imageView.setFitWidth(8);
        setLayout(imageView, vector);

        pane.getChildren().add(imageView);
    }

    public void setLayout(ImageView imageView, Vector vector) {
        imageView.setLayoutX(vector.getX() * 8);
        imageView.setLayoutY(vector.getY() * 8);
    }

    public Vector getVector() {
        return vector;
    }

    public void update(Board board) {
        Random random = new Random();

        Vector nextPos;
        int pos;
        do {
            pos = random.nextInt(4);

            nextPos = vector.add(Vector.getDirection(pos));
        } while(board.checkBoundaries(nextPos) && board.getField(nextPos) instanceof Wall);

        vector = nextPos;
        setLayout(imageView, vector);

        board.checkCollision(vector);
    }
}
