package views;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import models.Vector;

public class ViewField extends ImageView {

    private static final String path = "file:./../res/textures/texture.png";
    private static final Image image = new Image(path);

    private static final int START_OFFSET_COL = 28;
    private static final int START_OFFSET_ROW = 3;

    public ViewField() {
        super();
    }

    public void inflate(Pane stackPane, Vector vector, char type) {
        int normalized = ((int) type) - ((int) 'A');

        int col = normalized % 16;
        int row = (normalized - col) / 16;

        Image icon = new WritableImage(image.getPixelReader(),
                START_OFFSET_COL * 8 + 1 + col * 9,
                (START_OFFSET_ROW + row) * 9,
                8,
                8);

        setImage(icon);
        setLayoutX(vector.getY() * 8);
        setLayoutY(vector.getX() * 8);

        stackPane.getChildren().add(this);
    }
}
