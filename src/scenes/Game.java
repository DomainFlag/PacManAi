package scenes;

import controllers.Board;
import controllers.Playground;
import core.Scenemator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.GameSettings;
import views.DrawingView;
import views.TextView;

public class Game extends ViewScene implements Board.OnGameOver {

    private Board board;
    private GameSettings gameSettings = new GameSettings();

    private static final int GAME_ANIMATION = 150000000;

    public Game(Scenemator scenemator) {
        super(scenemator, "Pac-Man");
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
        setAnimationTime(GAME_ANIMATION);

        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setAlignment(Pos.CENTER);

        pane.setCenter(vBox);

        TextView textBerserkView = new TextView("Berserk Mode", 16, Color.WHITE, 16);
        textBerserkView.inflate(vBox);

        TextView textScoreView = new TextView("Score", 16, Color.WHITE, 16);
        textScoreView.inflate(vBox);

        HBox hGameBox = new HBox();
        hGameBox.setAlignment(Pos.CENTER);
        hGameBox.setFillHeight(true);
        vBox.getChildren().add(hGameBox);

        // Actual Game Play
        Pane gamePane = new Pane();
        hGameBox.getChildren().add(gamePane);

        Playground playground = new Playground();
        playground.setStyle(gameSettings.getStyle());
        playground.generateFields(gameSettings.getFile());
        playground.generatePlaygroundSnapshot(gamePane,16);

        DrawingView drawingView = new DrawingView(playground.getSnapshot());
        drawingView.setRoot(0);

        playground.inflate(drawingView);
        playground.addObserver(drawingView);

        // Adding the map snapshot below the inflated points views
        drawingView.inflate(gamePane, 0);

        board = new Board(this, playground);
        board.addBerserkView(textBerserkView);
        board.addObserver(textScoreView);
        board.createCharacters(gamePane);
    }

    public void onAttachGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    @Override
    public void onKeySceneListener(KeyCode keyCode) {
        if(keyCode == KeyCode.ESCAPE) {
            onBackPressed();
        }

        board.onUpdateKeyListener(keyCode);
    }

    @Override
    public void onAnimatorCallback() {
        board.onUpdatePhantoms();
        board.onUpdatePacMan();
    }

    @Override
    public void onGameOver(String message) {
        ViewScene viewScene = onBackPressed();
        if(viewScene instanceof Menu) {
            ((Menu) viewScene).setState(message);
        }
    }
}
