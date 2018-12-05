package scenes;

import controllers.Board;
import core.Constants;
import core.Scenemator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Field;
import models.Vector;
import views.TextView;

public class Game extends ViewScene implements Board.OnGameOver {

    private Board board;

    public Game(Scenemator scenemator) {
        super(scenemator, "Pac-Man");
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
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

        Pane pane1 = new Pane();
        hGameBox.getChildren().add(pane1);

        board = new Board(this);
        board.addBerserkView(textBerserkView);
        board.addObserver(textScoreView);

        create(pane1, board);
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

    public void create(Pane pane, Board board) {
        Vector dimension = board.getDimension().multiply(Constants.TILE_DIMEN);

        Canvas canvas = new Canvas(dimension.getX(), dimension.getY());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for(int i = 0; i < board.fields.length; i++) {
            for(int g = 0; g < board.fields[i].length; g++) {
                Field field = board.fields[i][g];

                Vector vector = new Vector(i, g);
                Image image = field.inflate();

                field.render(pane);

                graphicsContext.drawImage(image,
                        vector.getX() * Constants.TILE_DIMEN,
                        vector.getY() * Constants.TILE_DIMEN,
                        Constants.TILE_DIMEN,
                        Constants.TILE_DIMEN);
            }
        }

        WritableImage writableImage = canvas.snapshot(null, null);
        pane.getChildren().add(0, new ImageView(writableImage));

        board.createCharacters(pane);
    }

    @Override
    public void onGameOver(String message) {
        ViewScene viewScene = onBackPressed();
        if(viewScene instanceof Menu) {
            ((Menu) viewScene).setState(message);
        }
    }
}
