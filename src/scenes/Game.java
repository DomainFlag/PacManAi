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
import javafx.scene.shape.Rectangle;
import models.Field;
import models.Vector;
import tools.Log;
import views.LinearLayoutView;
import views.WindowBarView;

public class Game extends ViewScene implements Board.OnGameOver {

    private Board board;

    private int points = 0;

    public Game(Scenemator scenemator) {
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
        linearVerticalLayoutView.setLayoutGravity(Pos.CENTER_RIGHT);

        pane.setCenter(linearVerticalLayoutView.getNode());

        AnchorPane pane1 = new AnchorPane();
        linearVerticalLayoutView.add(pane1);

        board = new Board(this, 28, 8);

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
        Vector dimension = board.getDimension().multiply(8);

        Canvas canvas = new Canvas(dimension.getX(), dimension.getY());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for(int i = 0; i < board.fields.length; i++) {
            for(int g = 0; g < board.fields[i].length; g++) {
                Field field = board.fields[i][g];

                Vector vector = new Vector(i, g);
                Image image = field.inflate();

                field.render(pane);

                graphicsContext.drawImage(image,
                        vector.getX() * Constants.TILE_DIMEN_WIDTH,
                        vector.getY() * Constants.TILE_DIMEN_HEIGHT,
                        Constants.TILE_DIMEN_WIDTH,
                        Constants.TILE_DIMEN_HEIGHT);
            }
        }

        WritableImage writableImage = canvas.snapshot(null, null);
        pane.getChildren().set(0, new ImageView(writableImage));

        board.createCharacters(pane);
    }

    @Override
    public void onGameOver() {
        onBackPressed();
    }
}
