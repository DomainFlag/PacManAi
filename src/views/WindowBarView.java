package views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Vector;

import java.awt.*;

public class WindowBarView extends HBox {

    public WindowBarView(Scene scene, String title) {
        TextView textView = new TextView(title, 18, Color.WHITE, 16);
        textView.inflate(this);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        getChildren().add(region);

        IconView iconMinimizeView = new IconView(
                "file:res/icons/window-minimize.png",
                12,
                12,
                12,
                event -> {
                    if(event.getButton().equals(MouseButton.PRIMARY)) {
                        ( (Stage) scene.getWindow()).setIconified(true);
                    }
                });
        iconMinimizeView.inflate(this);

        IconView iconCloseView = new IconView(
                "file:res/icons/window-close.png",
                12,
                12,
                12,
                event -> {
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

    class Delta {
        double x, y;
    }
}
