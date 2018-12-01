package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import tools.Log;

public class Point extends Field {

    private ImageView imageView;
    private Pane pane;

    private char floatedType;

    public Point(Vector vector, char type) {
        super(vector, 'm');

        floatedType = type;
    }

    @Override
    public void render(Pane pane) {
        this.pane = pane;

        imageView = new ImageView(inflate(floatedType));
        imageView.setFitWidth(8);
        imageView.setFitHeight(8);


        setLayout(imageView, getVector());
        pane.getChildren().add(imageView);
    }

    @Override
    public int remove() {
        if(pane.getChildren().remove(imageView)) {
            return 15;
        }

        return 0;
    }

    public void setLayout(ImageView imageView, Vector vector) {
        imageView.setLayoutX(vector.getX() * 8);
        imageView.setLayoutY(vector.getY() * 8);
    }
}
