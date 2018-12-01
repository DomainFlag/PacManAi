package scenes;

import core.Constants;
import core.Scenemator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import views.LinearLayoutView;
import views.TextView;
import views.WindowBarView;

public class Menu extends ViewScene {

    public Menu(Scenemator scenemator) {
        super(scenemator);
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
        Rectangle rectangle = new Rectangle(Constants.DIM_X, Constants.DIM_Y);
        rectangle.setFill(Color.BLACK);
        pane.getChildren().add(rectangle);

        WindowBarView windowBarView = new WindowBarView(getScene());
        pane.setTop(windowBarView);

        LinearLayoutView linearVerticalLayoutView = new LinearLayoutView(
                LinearLayoutView.LayoutWidth.MATCH_PARENT,
                LinearLayoutView.LayoutHeight.WRAP_CONTENT,
                LinearLayoutView.Orientation.VERTICAL,
                LinearLayoutView.Gravity.CENTER);
        linearVerticalLayoutView.setLayoutGravity(Pos.CENTER);

        pane.setCenter(linearVerticalLayoutView.getNode());

        TextView textView = new TextView("START", 16);
        textView.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                startGame();
            }
        });
        linearVerticalLayoutView.add(textView);
    }

    private void startGame() {
        Game game = new Game(getScenemator());
        addScene(game);
    }

    @Override
    public void onKeySceneListener(KeyCode keyCode) {
        if(keyCode == KeyCode.ENTER)
            startGame();
    }

    @Override
    public void onAnimatorCallback() {

    }
}
