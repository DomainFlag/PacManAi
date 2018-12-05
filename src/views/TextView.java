package views;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.beans.EventHandler;
import java.util.Observable;
import java.util.Observer;

public class TextView extends Text implements Observer {

    private StackPane root;

    public TextView(String text, int textSize, Color color, Insets padding) {
        super(text);

        setFill(color);
        setFont(getFont(textSize));

        root = new StackPane();
        root.setPadding(padding);
        root.getChildren().add(this);
    }

    public TextView(String text, int textSize, Color color, int padding) {
        this(text, textSize, color, new Insets(padding));
    }

    private Font getFont(int size) {
        return Font.loadFont("file:res/fonts/Brandon_reg.otf", size);
    }

    private Font getFont(double size) {
        return getFont((int) size);
    }

    public void setOnHover(Color color) {
        Paint defaultPaint = getFill();
        Font defaultFont = getFont();

        setOnMouseEntered(event -> {
            setFill(color);
            setFont(getFont(defaultFont.getSize() + 4));
        });

        setOnMouseExited(event -> {
            setFill(defaultPaint);
            setFont(defaultFont);
        });
    }

    public void inflate(Pane pane) {
        pane.getChildren().add(root);
    }

    public void remove(Pane pane) {
        pane.getChildren().remove(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String) {
            setText((String) arg);
        }
    }
}
