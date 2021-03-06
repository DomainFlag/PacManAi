import core.Scenemator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Scenemator scenemator = new Scenemator(primaryStage);
        scenemator.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
