package views;

import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.awt.*;

public class WindowBarView extends HBox {

    public WindowBarView(Scene scene) {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        getChildren().add(region);

        IconView iconMinimizeView = new IconView("file:res/icons/window-minimize.png", 12, 12, 12);
        iconMinimizeView.getNode().setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                ( (Stage) scene.getWindow()).setIconified(true);
            }
        });
        getChildren().add(iconMinimizeView.getNode());


        IconView iconCloseView = new IconView("file:res/icons/window-close.png", 12, 12, 12);
        iconCloseView.getNode().setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                Stage stage = (Stage) scene.getWindow();
                stage.close();
            }
        });
        getChildren().add(iconCloseView.getNode());
    }
}
