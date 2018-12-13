package scenes;

import controllers.Playground;
import controllers.Tools;
import core.Scenemator;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Field;
import models.Point;
import models.Vector;
import tools.Log;
import views.EditTextView;
import views.FieldView;
import views.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Creator extends ViewScene {

    private Vector dimension = new Vector(Playground.MAX_DIMEN, Playground.MAX_DIMEN);
    private Character currentType = 'A';

    public Creator(Scenemator scenemator) {
        super(scenemator, "Board Creator");
    }

    @Override
    public void onCreateScene(Scene scene, BorderPane pane) {
        VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setAlignment(Pos.CENTER);

        pane.setCenter(vBox);

        // Parent layout
        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        hBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(hBox, Priority.ALWAYS);

        vBox.getChildren().add(hBox);

        // Preview
        VBox vPreviewBox = new VBox();
        vPreviewBox.setFillWidth(false);
        vPreviewBox.setAlignment(Pos.TOP_LEFT);

        hBox.getChildren().add(vPreviewBox);

        Playground playground = new Playground();
        createPreview(vPreviewBox, playground);

        // Creator settings
        VBox vSettingsBox = new VBox();
        vSettingsBox.setFillWidth(false);
        vSettingsBox.setAlignment(Pos.TOP_LEFT);
        vSettingsBox.setPadding(new Insets(16));

        hBox.getChildren().add(vSettingsBox);

        createSettings(vSettingsBox, playground);

        // Tools parts
        VBox vToolsBox = new VBox();
        vToolsBox.setFillWidth(false);
        vToolsBox.setAlignment(Pos.CENTER_LEFT);
        vToolsBox.setPadding(new Insets(16));

        vBox.getChildren().add(vToolsBox);

        createToolset(vToolsBox);
    }

    private void createPreview(Pane pane, Playground playground) {
        Pane root = new Pane();

        VBox.setMargin(pane, new Insets(16));
        VBox.setMargin(root, new Insets(16));

        TextView textView = new TextView("Custom board:", 16, Color.WHITE, 16);
        textView.inflate(pane);

        pane.getChildren().add(root);

        playground.generatePlaygroundCreator(root, field -> field.resolve(currentType));
    }

    private void createSettings(Pane pane, Playground playground) {
        List<File> files = Tools.generateMaps();
        List<String> filesDecoded = Tools.generateMapsDecoded(files);

        // Adjusting the board settings label
        TextView textLabelView = new TextView("Board settings:", 16, Color.WHITE, 0);
        textLabelView.inflate(pane);

        // Adjusting the width
        EditTextView editTextWidthView = new EditTextView("16", "value...", "Board width:");
        editTextWidthView.onAttachEditTextChangeListener(value -> {
            if(!value.isEmpty()) {
                int width = Integer.valueOf(value);

                dimension.setX(width);
                playground.adjustPlayground(dimension);
            }
        });
        editTextWidthView.inflate(pane);

        // Adjusting the height
        EditTextView editTextHeightView = new EditTextView("12", "value...", "Board height");
        editTextHeightView.onAttachEditTextChangeListener(value -> {
            if(!value.isEmpty()) {
                int height = Integer.valueOf(value);

                dimension.setY(height);
                playground.adjustPlayground(dimension);
            }
        });
        editTextHeightView.inflate(pane);

        // Filling up everything with points
        TextView textPointsView = new TextView("Fill blank with points", 16, Color.LIGHTGRAY,
                new Insets(16, 0, 16, 0));
        textPointsView.setOnMouseClicked(event -> {
            playground.fillPoints();
        });
        textPointsView.setOnHover(Color.WHITE);
        textPointsView.inflate(pane);

        // Reusing the old maps
        TextView textMapsView = new TextView("Your maps", 16, Color.LIGHTGRAY,
                new Insets(16, 0, 16, 0));
        textMapsView.inflate(pane);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setBackground(Background.EMPTY);

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int optionItemSelected = newValue.intValue() - 1;
            if(optionItemSelected >= 0) {
                File file = files.get(optionItemSelected);

//                Playground playground = new Playground();
//                playground.generateFields(file);
//
//                editTextHeightView.setText(String.valueOf(playground.getDimension().getX()));
//                editTextWidthView.setText(String.valueOf(playground.getDimension().getY()));
//
//                playground.resolvePlayground(playground);
            }
        });

        if(!files.isEmpty()) {
            filesDecoded.add(0, "New");

            choiceBox.setItems(FXCollections.observableArrayList(filesDecoded));
            choiceBox.getSelectionModel().selectFirst();

            pane.getChildren().add(choiceBox);
        }

        // Saving current map
        TextView textSaveView = new TextView("Save the map", 16, Color.LIGHTGRAY,
                new Insets(48, 0, 16, 0));

        textSaveView.setOnMouseClicked(event -> {
            playground.saveBoard("map_" + (files.size() + 1));
        });

        textSaveView.setOnHover(Color.WHITE);
        textSaveView.inflate(pane);
    }

    private void createToolset(Pane pane) {
        TextView textView = new TextView("Available Parts:", 16, Color.WHITE, 16);
        textView.inflate(pane);

        Pane parts = new Pane();
        pane.getChildren().add(parts);
        VBox.setMargin(parts, new Insets(16));

        for(int i = 0; i < 3; i++) {
            for(int g = 0; g < 16; g++) {
                int pos = i * 16 + g;

                char type = (char) (pos + 65);

                Image image = Playground.generateTileImage(type, 0);
                ImageView imageView = new ImageView(image);
                imageView.setLayoutX(g * 24);
                imageView.setLayoutY(i * 24);
                imageView.setFitWidth(16.0f);
                imageView.setFitHeight(16.0f);

                imageView.setOnMouseClicked((event) -> {
                    if(event.getButton() == MouseButton.PRIMARY) {
                        currentType = type;
                    }
                });

                parts.getChildren().add(imageView);
            }
        }
    }

    @Override
    public void onKeySceneListener(KeyCode keyCode) {
        if(keyCode == KeyCode.ESCAPE) {
            onBackPressed();
        }
    }
}
