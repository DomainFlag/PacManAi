package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import models.Vector;

public class ViewField extends ImageView {

    public ViewField() {
        super();
    }

    public void inflate(Pane stackPane, Image icon, Vector vector) {
        setImage(icon);
        setLayoutX(vector.getX() * 8);
        setLayoutY(vector.getY() * 8);

        stackPane.getChildren().add(this);
    }
}
