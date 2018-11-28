package views;

import controllers.Board;
import javafx.scene.layout.Pane;
import models.Vector;

public class PlayScene {

    public PlayScene() {}

    public void create(Pane pane, Board board) {
        for(int i = 0; i < board.fields.length; i++) {
            for(int g = 0; g < board.fields[i].length; g++) {
                ViewField viewField = new ViewField();
                Vector vector = new Vector(i, g);

                viewField.inflate(pane, board.fields[i][g].inflate(), vector);
            }
        }

        board.createCharacters(pane);
    }
}
