package views;

import interfaces.Inflater;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import core.Log;

import java.util.Observable;
import java.util.Observer;

public class WindowBarView extends HBox implements Inflater, Observer {

    public WindowBarView(Scene scene, String title) {
        TextView textView = new TextView(title, 18, Color.WHITE, 16);
        textView.inflate(this);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        getChildren().add(region);

        DrawingView iconMinimizeView = new DrawingView(
                "file:res/icons/window-minimize.png",
                12,
                12,
                12);
        iconMinimizeView.setOnMouseClickListener(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Log.v("Done");
                ( (Stage) scene.getWindow()).setIconified(true);
            }
        });

        iconMinimizeView.inflate(this);

        DrawingView iconCloseView = new DrawingView(
                "file:res/icons/window-close.png",
                12,
                12,
                12);
        iconCloseView.setOnMouseClickListener(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Stage stage = (Stage) scene.getWindow();
                stage.close();
            }
        });

        iconCloseView.inflate(this);
    }

    public void setStage(Stage stage) {
        Delta dragDelta = new Delta();

        this.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.x = stage.getX() - mouseEvent.getScreenX();
            dragDelta.y = stage.getY() - mouseEvent.getScreenY();
        });

        this.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + dragDelta.x);
            stage.setY(mouseEvent.getScreenY() + dragDelta.y);
        });
    }

    @Override
    public void inflate(Pane pane) {
        pane.getChildren().add(0, this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    class Delta {
        double x, y;
    }
}
