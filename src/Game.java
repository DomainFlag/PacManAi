import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import controllers.Board;
import views.PlayScene;

import static javafx.scene.input.KeyCode.ESCAPE;

public class Game {

    private AnimationTimer animationTimer;

    private Stage primaryStage;

    public Game(Stage primaryStage, String title) {
        this.primaryStage = primaryStage;

        init(title);
    }

    public void init(String title) {
        Group root = new Group();
        Scene scene = new Scene(root, Settings.DIM_X, Settings.DIM_Y);

        Pane pane = new Pane();
        root.getChildren().add(pane);

        Board board = new Board( 6, 6);
        PlayScene playScene = new PlayScene();
        playScene.create(pane, board);

        setOnKeyListener(scene);
        setGameAnimator();

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void setGameAnimator() {
        LongProperty lastUpdateTime = new SimpleLongProperty(0);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                long elapsedTime = timestamp - lastUpdateTime.get();
                if(elapsedTime > 60000) {
                    lastUpdateTime.set(timestamp);
                }
            }
        };

        animationTimer.start();
    }

    private void setOnKeyListener(Scene scene) {
        scene.setOnKeyPressed((event -> {
            if(event.getCode() == ESCAPE) {
                if(animationTimer != null) {
                    animationTimer.stop();

                    primaryStage.close();
                }
            }
        }));
    }
}
