package views;

import interfaces.Inflater;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Observable;
import java.util.Observer;

public class EditTextView extends TextField implements Inflater, Observer {

    public interface OnEditTextChangeListener {
        void onEditTextChangeListener(String value);
    }

    private OnEditTextChangeListener onEditTextChangeListener = null;

    private Label label = new Label();
    private Label error = new Label();

    public EditTextView(String text, String placeholder, String label) {
        super(text);

        setBackground(Background.EMPTY);
        setBorder(new Border(new BorderStroke(Color.LIGHTSLATEGRAY,
                BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT)));
        setPromptText(placeholder);
        textProperty().addListener((observable, oldValue, newValue) -> {
            if(onEditTextChangeListener != null) {
                if(newValue.matches("\\d*")) {
                    String value = newValue.replaceAll("[^\\d]", "");
                    onChangeErrorLabel(null);

                    onEditTextChangeListener.onEditTextChangeListener(value);
                } else {
                    onChangeErrorLabel("Numerical value required!");
                }
            }
        });
        setStyle("-fx-text-inner-color: white");

        onSetTitleLabel(label);
        onSetErrorLabel();
    }

    public void onChangeErrorLabel(String errorMessage) {
        error.setText(errorMessage);
        error.setVisible(!((errorMessage == null) || errorMessage.isEmpty()));
    }

    private void onSetErrorLabel() {
        error.setLabelFor(this);
        error.setTextFill(Color.RED);
        error.setFont(new Font(10));
        error.setPadding(new Insets(4, 0, 4, 0));
    }

    public void onAttachEditTextChangeListener(OnEditTextChangeListener onEditTextChangeListener) {
        this.onEditTextChangeListener = onEditTextChangeListener;
    }

    public void onSetTitleLabel(String labelText) {
        label.setText(labelText);
        label.setLabelFor(this);
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(new Font(12));
        label.setPadding(new Insets(8, 0, 8, 0));

        if(labelText.isEmpty())
            label.setVisible(false);
        else label.setVisible(true);
    }

    @Override
    public void inflate(Pane pane) {
        pane.getChildren().add(label);
        pane.getChildren().add(this);
        pane.getChildren().add(error);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof String) {
            setText((String) arg);
        }
    }
}