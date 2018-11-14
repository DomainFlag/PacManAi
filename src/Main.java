import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.ESCAPE;

public class Main extends Application {

    private AnimationTimer animationTimer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Settings.DIM_X, Settings.DIM_Y);


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

        scene.setOnKeyPressed((event -> {
            if(event.getCode() == ESCAPE) {
                if(animationTimer != null) {
                    animationTimer.stop();
                    primaryStage.close();
                }
            }
        }));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
