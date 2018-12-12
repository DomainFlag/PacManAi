package views;

import interfaces.Inflater;
import interfaces.ItemSelectable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import tools.Log;

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

    public void setOnHover(Color color, double scaleFactor) {
        Scale scale = new Scale();
        scale.setPivotX(getBoundsInLocal().getWidth() / 2.0f);
        scale.setPivotY(0);
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        root.setOnMouseEntered(event -> {
            getTransforms().add(scale);

            setFill(color);
        });

        root.setOnMouseExited(event -> {
            getTransforms().remove(scale);

            setFill(defaultPaint);
        });
    }

    public void setOnHover(Color color) {
        setOnHover(color, 1.25d);
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
