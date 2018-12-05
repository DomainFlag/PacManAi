package models;

import controllers.Board;
import core.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public abstract class Spirit {

    private Pane root;
    private List<Image> images;
    private Vector vector;
    private ImageView imageView;

    public boolean active = true;

    public Spirit(Vector vector, List<Image> images) {
        this.vector = vector;
        this.images = images;
    }

    public void render(Pane pane) {
        this.root = pane;

        imageView = new ImageView(images.get(0));
        imageView.setFitWidth(Constants.TILE_DIMEN);
        imageView.setFitHeight(Constants.TILE_DIMEN);

        updateLayout();

        pane.getChildren().add(imageView);
    }

    public void disable() {
        root.getChildren().remove(imageView);
        active = false;
    }

    public void updateLayout() {
        imageView.setLayoutX(vector.getX() * Constants.TILE_DIMEN);
        imageView.setLayoutY(vector.getY() * Constants.TILE_DIMEN);
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void updateSpirit(Board board) {
        if(active)
            update(board);
    }

    public abstract void update(Board board);
}
