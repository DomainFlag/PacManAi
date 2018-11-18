import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game(primaryStage, "Pac-Man");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
