package scenes;

import core.Constants;
import core.Scenemator;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import tools.Log;
import views.WindowBarView;

import javax.swing.text.View;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.input.KeyCode.ESCAPE;

public abstract class ViewScene {

    public interface TimeOutListener {
        void timeOutListenerCallback();
    }

    private Scenemator scenemator;
    private Scene scene;
    private BorderPane root;

    private AnimationTimer animationTimer;
    private List<TimeOut> timeOutListeners = new ArrayList<>();

    private boolean activeScene = false;
    private int animationTime = 100000000;

    public ViewScene(Scenemator scenemator, String title) {
        this.scenemator = scenemator;

        root = new BorderPane();
        scene = new Scene(root, Constants.DIM_X, Constants.DIM_Y);

        Rectangle rectangle = new Rectangle(Constants.DIM_X, Constants.DIM_Y);
        rectangle.setFill(Color.BLACK);
        root.getChildren().add(rectangle);

        WindowBarView windowBarView = new WindowBarView(getScene(), title);
        windowBarView.setStage(scenemator.getPrimaryStage());
        root.setTop(windowBarView);
    }

    public void showScene(Stage primaryStage) {
        onCreateScene(scene, root);
        animateScene();
        setOnKeySceneListener(scene);

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

    public ViewScene onBackPressed() {
        activeScene = false;

        return scenemator.onBack();
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
                for(int i = timeOutListeners.size() - 1; i >= 0; i--) {
                    TimeOut timeOut = timeOutListeners.get(i);

                    if(timeOut.resolve(timestamp))
                        timeOutListeners.remove(timeOut);
                }

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

    public void registerTimeOutListener(TimeOutListener timeOutListener, long duration) {
        timeOutListeners.add(new TimeOut(timeOutListener, duration));
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

                onKeySceneListener(keyCode);
            }
        }));
    }

    public abstract void onCreateScene(Scene scene, BorderPane root);

    public abstract void onKeySceneListener(KeyCode keyCode);

    public abstract void onAnimatorCallback();

    private class TimeOut {
        private long duration;
        private LongProperty lastUpdateTime = new SimpleLongProperty(0);
        private TimeOutListener timeOutListener;
        private boolean active = false;

        private TimeOut(TimeOutListener timeOutListener, long duration) {
            this.timeOutListener = timeOutListener;
            this.duration = duration;
        }

        private boolean resolve(long timestamp) {
            if(active) {
                if(timestamp - lastUpdateTime.get() > duration) {
                    timeOutListener.timeOutListenerCallback();

                    return true;
                }
            } else {
                lastUpdateTime.set(timestamp);
                active = true;
            }

            return false;
        }
    }
}
