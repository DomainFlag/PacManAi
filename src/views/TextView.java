package views;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextView extends Text {

    private static final Font font = Font.loadFont("file:res/fonts/Brandon_reg.otf", 32);

    private StackPane root;

    public TextView(String text, int padding) {
        super(text);
        setFill(Color.WHITE);
        setFont(font);

        root = new StackPane();
        root.setPadding(new Insets(padding));
        root.getChildren().add(this);
    }

    public Node getNode() {
        return root;
    }
}
