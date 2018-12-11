package models;

import core.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.util.Observable;

public abstract class Field extends Observable {

    private static final String path = "file:./../res/textures/texture.png";
    private static final Image image = new Image(path);

    private Vector vector;

    private static final int START_OFFSET_COL = 28;
    private static final int START_OFFSET_ROW = 6;

    private char type;

    public Field(Vector vector, char type) {
        this.type = type;
        this.vector = vector;
    }

    public char getType() {
        return type;
    }

    public Vector getVector() {
        return vector;
    }

    public void render(Pane pane) {}

    public void setLayout(ImageView imageView, Vector vector) {
        imageView.setLayoutX(vector.getX() * Constants.TILE_DIMEN_DEFAULT);
        imageView.setLayoutY(vector.getY() * Constants.TILE_DIMEN_DEFAULT);
    }

    public static Image inflate(char type) {
        int normalized = ((int) type) - ((int) 'A');

        int row = normalized % 16;
        int col = (normalized - row) / 16;

        return new WritableImage(image.getPixelReader(),
                START_OFFSET_COL * 8 + 1 + row * 9,
                (START_OFFSET_ROW + col) * 9,
                8,
                8);
    }

    public Image inflate() {
        return Field.inflate(this.type);
    }
}
