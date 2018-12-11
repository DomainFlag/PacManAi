package views;

import interfaces.Inflater;
import interfaces.ItemSelectable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.beans.EventHandler;
import java.util.Observable;
import java.util.Observer;

public class TextView extends Text implements Observer, Inflater {

    private StackPane root;
    private Paint defaultPaint;

    public TextView(String text, int textSize, Color color, Insets padding) {
        super(text);

        defaultPaint = color;

        setFill(defaultPaint);
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

    public void setDefaultPaint(Paint defaultPaint) {
        this.defaultPaint = defaultPaint;

        setFill(defaultPaint);
    }

    @Override
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
