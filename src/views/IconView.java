package views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.Stack;

public class IconView extends ImageView {

    private StackPane root;

    public IconView(String path, int width, int height, int padding) {
        super(path);
        setFitWidth(width);
        setFitHeight(height);

        root = new StackPane();
        root.setPadding(new Insets(padding));
        root.getChildren().add(this);
    }

    public Node getNode() {
        return root;
    }
}
