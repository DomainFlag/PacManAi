package views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EditTextView extends TextField {

    public interface OnEditTextChangeListener {
        void onEditTextChangeListener(String value);
    }

    private OnEditTextChangeListener onEditTextChangeListener = null;

    private Label label;
    private Label error;

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

                    onEditTextChangeListener.onEditTextChangeListener(value);

                    onChangeErrorLabel("");
                } else {
                    onChangeErrorLabel("Numerical value required!");
                }
            }
        });
        setStyle("-fx-text-inner-color: white");

        onSetTitleLabel(label);
        onSetErrorLabel("");
    }

    public void onChangeErrorLabel(String errorMessage) {
        error.setText(errorMessage);
        error.setVisible(!errorMessage.isEmpty());
    }

    public void onSetErrorLabel(String errorMessage){
        error = new Label(errorMessage);
        error.setLabelFor(this);
        error.setTextFill(Color.RED);
        error.setFont(new Font(10));
        error.setPadding(new Insets(4, 0, 4, 0));

        if(errorMessage.isEmpty())
            error.setVisible(false);
    }

    public void onAttachEditTextChangeListener(OnEditTextChangeListener onEditTextChangeListener) {
        this.onEditTextChangeListener = onEditTextChangeListener;
    }

    public void onSetTitleLabel(String labelText) {
        label = new Label(labelText);
        label.setLabelFor(this);
        label.setTextFill(Color.LIGHTGRAY);
        label.setFont(new Font(12));
        label.setPadding(new Insets(8, 0, 8, 0));
    }

    public void inflate(Pane node) {
        node.getChildren().add(label);
        node.getChildren().add(this);
        node.getChildren().add(error);
    }
}
