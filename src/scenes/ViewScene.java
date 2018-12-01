package scenes;

import core.Constants;
import core.Scenemator;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.ESCAPE;

public abstract class ViewScene {

    private Scenemator scenemator;
    private Scene scene;
    private BorderPane root;

    private AnimationTimer animationTimer;

    private boolean activeScene = false;
    private int animationTime = 100000000;

    public ViewScene(Scenemator scenemator) {
        this.scenemator = scenemator;

        root = new BorderPane();
        scene = new Scene(root, Constants.DIM_X, Constants.DIM_Y);

        onCreateScene(scene, root);
        animateScene();
        setOnKeySceneListener(scene);
    }

    public void showScene(Stage primaryStage) {
        primaryStage.setScene(scene);
        primaryStage.show();

        activeScene = true;
    }

    public void addScene(ViewScene scene) {
        scenemator.add(scene);
    }

    public void pauseScene() {
        activeScene = false;
    }

    public Scene getScene() {
        return scene;
    }

    public void onBackPressed() {
        scenemator.onBack();
    }

    public Scenemator getScenemator() {
        return scenemator;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public void setScene(Scene scene) {
        scenemator.start();
    }

    private void animateScene() {
        LongProperty lastUpdateTime = new SimpleLongProperty(0);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                long elapsedTime = timestamp - lastUpdateTime.get();
                if(elapsedTime > animationTime) {
                    lastUpdateTime.set(timestamp);

                    if(activeScene)
                        onAnimatorCallback();
                }
            }
        };

        animationTimer.start();
    }

    private void setOnKeySceneListener(Scene scene) {
        scene.setOnKeyPressed((event -> {
            if(activeScene) {
                KeyCode keyCode = event.getCode();
                if(keyCode == ESCAPE) {
                    if(animationTimer != null) {
                        animationTimer.stop();
                    }
                }

                if(activeScene)
                    onKeySceneListener(keyCode);
            }
        }));
    }

    public abstract void onCreateScene(Scene scene, BorderPane root);

    public abstract void onKeySceneListener(KeyCode keyCode);

    public abstract void onAnimatorCallback();
}
