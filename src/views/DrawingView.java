package views;

import interfaces.Inflater;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Observable;
import java.util.Observer;

public class DrawingView extends ImageView implements Inflater, Observer {

    private StackPane root;

    public DrawingView(int posX, int posY, int width, int height, int padding) {
        setLayoutX(posX);
        setLayoutY(posY);

        setFitWidth(width);
        setFitHeight(height);

        setRoot(padding);
    }

    public DrawingView(String path, int width, int height, int padding) {
        super(path);

        setFitWidth(width);
        setFitHeight(height);

        setRoot(padding);
    }

    public DrawingView(String path, int padding) {
        super(path);

        setRoot(padding);
    }

    public DrawingView() {
        super();

        setRoot(0);
    }

    public DrawingView(String url) {
        super(url);

        setRoot(0);
    }

    public DrawingView(Image image) {
        super(image);

        setRoot(0);
    }

    public void setOnMouseClickListener(EventHandler<MouseEvent> eventEventHandler) {
        root.setOnMouseClicked(eventEventHandler);
    }

    public void setRoot(int padding) {
        root = new StackPane();
        root.setPadding(new Insets(padding));
        root.getChildren().add(this);
    }

    @Override
    public void inflate(Pane pane) {
        pane.getChildren().add(root);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Image)
            setImage((Image) arg);
    }
}
