package scenes;

import core.Scenemator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import models.Field;
import models.Vector;
import tools.Log;
import views.EditTextView;
import views.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Creator extends ViewScene {

    private Character currentType = null;

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

        CreatorBoard creatorBoard = new CreatorBoard(16, 12, 16, 16);
        creatorBoard.inflateBoard();
        creatorBoard.inflateBoard(vPreviewBox);

        // Creator settings
        VBox vSettingsBox = new VBox();
        vSettingsBox.setFillWidth(false);
        vSettingsBox.setAlignment(Pos.TOP_LEFT);
        vSettingsBox.setPadding(new Insets(16));

        hBox.getChildren().add(vSettingsBox);

        createSettings(vSettingsBox, creatorBoard);

        // Tools parts
        VBox vToolsBox = new VBox();
        vToolsBox.setFillWidth(false);
        vToolsBox.setAlignment(Pos.CENTER_LEFT);
        vToolsBox.setPadding(new Insets(16));

        vBox.getChildren().add(vToolsBox);

        createToolset(vToolsBox);
    }


    public void createSettings(Pane pane, CreatorBoard creatorBoard) {
        TextView textLabelView = new TextView("Board settings:", 16, Color.WHITE, 0);
        textLabelView.inflate(pane);

        EditTextView editTextWidthView = new EditTextView("16", "value...", "Board width:");
        editTextWidthView.onAttachEditTextChangeListener(value -> {
            if(!value.isEmpty()) {
                int width = Integer.valueOf(value);

                creatorBoard.adjustWidth(width);
            }
        });
        editTextWidthView.inflate(pane);

        EditTextView editTextHeightView = new EditTextView("12", "value...", "Board height");
        editTextHeightView.onAttachEditTextChangeListener(value -> {
            if(!value.isEmpty()) {
                int height = Integer.valueOf(value);

                creatorBoard.adjustHeight(height);
            }
        });
        editTextHeightView.inflate(pane);

        TextView textSaveView = new TextView("Save the map", 16, Color.LIGHTGRAY,
                new Insets(16, 0, 16, 0));
        textSaveView.setOnMouseClicked(event -> {
            creatorBoard.saveBoard("map_2");
        });
        textSaveView.setOnHover(Color.GREEN);
        textSaveView.inflate(pane);
    }

    public void createToolset(Pane pane) {
        TextView textView = new TextView("Available Parts:", 16, Color.WHITE, 16);
        textView.inflate(pane);

        Pane parts = new Pane();
        pane.getChildren().add(parts);
        VBox.setMargin(parts, new Insets(16));

        for(int i = 0; i < 3; i++) {
            for(int g = 0; g < 16; g++) {
                int pos = i * 16 + g;

                char type = (char) (pos + 65);

                Image image = Field.inflate(type);
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

    @Override
    public void onAnimatorCallback() {
    }

    private class Piece {
        private ImageView imageView;
        private char type;

        private Piece() {}

        private Piece(ImageView imageView, char type) {
            this.imageView = imageView;
            this.type = type;
        }

        private void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public void setType(char type) {
            this.type = type;
        }

        public char getType() {
            return type;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    private class CreatorBoard {
        private List<List<Piece>> board = new ArrayList<>();

        private Pane root = new Pane();

        private Vector dimension;
        private Vector size;

        private CreatorBoard(int width, int height, int widthCell, int heightCell) {
            dimension = new Vector(width, height);
            size = new Vector(widthCell, heightCell);
        }

        private CreatorBoard(Vector dimension, Vector size) {
            this.dimension = dimension;
            this.size = size;
        }

        private Piece createItem(int i, int g) {
            Piece piece = new Piece();
            piece.setType('n');

            ImageView imageView = new ImageView();
            imageView.setLayoutX(i * size.getX());
            imageView.setLayoutY(g * size.getY());
            imageView.setFitWidth(size.getX());
            imageView.setFitHeight(size.getY());
            imageView.setImage(Field.inflate('n'));

            imageView.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY) {
                    if(currentType != null) {
                        Image image = Field.inflate(currentType);
                        imageView.setImage(image);

                        piece.setType(currentType);
                    }
                }
            });

            piece.setImageView(imageView);
            root.getChildren().add(imageView);

            return piece;
        }

        private void inflateBoard(Pane pane) {
            VBox.setMargin(pane, new Insets(16));
            VBox.setMargin(root, new Insets(16));

            TextView textView = new TextView("Custom board:", 16, Color.WHITE, 16);
            textView.inflate(pane);

            pane.getChildren().add(root);
        }

        private void inflateBoard() {
            for(int i = 0; i < dimension.getX(); i++) {
                List<Piece> boardCol = new ArrayList<>();

                for(int g = 0; g < dimension.getY(); g++) {
                    boardCol.add(createItem(i, g));
                }

                board.add(boardCol);
            }
        }

        private void adjustWidth(int width) {
            adjust(new Vector(width, dimension.getY()));
        }

        private void adjustHeight(int height) {
            adjust(new Vector(dimension.getX(), height));
        }

        private void adjust(Vector vector) {
            if(dimension.isGreater(vector)) {
                adjustShrink(vector);
            } else {
                adjustExtend(vector);
            }

            dimension = vector;
        }

        private void adjustExtend(Vector vector) {
            int i;
            for(i = 0; i < dimension.getX(); i++) {
                List<Piece> boardCol = board.get(i);

                for(int g = boardCol.size(); g < vector.getY(); g++) {
                    boardCol.add(createItem(i, g));
                }
            }

            for(i = dimension.getX(); i < vector.getX(); i++) {
                List<Piece> boardCol = new ArrayList<>();

                for(int g = 0; g < vector.getY(); g++) {
                    boardCol.add(createItem(i, g));
                }

                board.add(boardCol);
            }
        }

        private void adjustShrink(Vector vector) {
            int i;
            for(i = 0; i < vector.getX(); i++) {
                List<Piece> boardCol = board.get(i);

                for(int g = vector.getY(); g < dimension.getY(); g++) {
                    Piece piece = boardCol.get(boardCol.size() - 1);

                    root.getChildren().remove(piece.getImageView());
                    boardCol.remove(piece);
                }
            }

            for(i = vector.getX(); i < dimension.getX(); i++) {
                List<Piece> boardCol = board.get(board.size() - 1);

                for(int g = 0; g < dimension.getY(); g++) {
                    Piece piece = boardCol.get(boardCol.size() - 1);

                    root.getChildren().remove(piece.getImageView());
                    boardCol.remove(piece);
                }

                board.remove(boardCol);
            }
        }

        private void saveBoard(String filename) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("res/maps/" + filename + ".txt");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(encodeBoard());
                bufferedWriter.close();
            } catch(IOException e) {
                Log.v(e.toString());
            }
        }

        private String encodeBoard() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(dimension.getX());
            stringBuilder.append(" ");
            stringBuilder.append(dimension.getY());
            stringBuilder.append("\n");
            for(int i = 0; i < dimension.getX(); i++) {
                List<Piece> boardCol = board.get(i);

                for(int g = 0; g < dimension.getY(); g++) {
                    stringBuilder.append(boardCol.get(g).getType());
                }

                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }
}
