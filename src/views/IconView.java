package views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.Stack;

public class IconView extends ImageView {

    private StackPane root;

    public IconView(String path, int width, int height, int padding, EventHandler<MouseEvent> event) {
        super(path);

        setFitWidth(width);
        setFitHeight(height);

        root = new StackPane();
        root.setPadding(new Insets(padding));
        root.getChildren().add(this);
        root.setOnMouseClicked(event);
    }

    public void inflate(Pane pane) {
        pane.getChildren().add(root);
    }
}
