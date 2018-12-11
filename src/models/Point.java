package models;

import com.sun.istack.internal.Nullable;
import core.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import tools.Log;

public class Point extends Field {

    private ImageView imageView;
    private Pane pane;

    private char floatedType;
    private int value;

    public boolean powerPoint;

    public Point(Vector vector, char type) {
        super(vector, 'm');

        floatedType = type;
        value = (int) Math.pow(10, floatedType - 'n' + 1);
        powerPoint = value > 10;
    }

    public char getFloatedType() {
        return floatedType;
    }

    @Override
    public void render(@Nullable Pane pane) {
        if(pane != null) {
            this.pane = pane;

            imageView = new ImageView(inflate(floatedType));
            imageView.setFitWidth(Constants.TILE_DIMEN_DEFAULT);
            imageView.setFitHeight(Constants.TILE_DIMEN_DEFAULT);


            setLayout(imageView, getVector());
            pane.getChildren().add(imageView);
        }
    }

    public void disable() {
        pane.getChildren().remove(imageView);

        value = 0;
    }

    public int getValue() {
        return value;
    }
}
