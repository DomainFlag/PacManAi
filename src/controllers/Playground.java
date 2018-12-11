package controllers;

import core.Constants;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import models.*;
import tools.Log;
import views.DrawingView;

import java.io.*;
import java.util.Observable;

public class Playground extends Observable {

    private static final String path = "file:./../res/textures/texture.png";

    private static final Image image = new Image(path);

    private static final int START_OFFSET_COL = 28;
    private static final int START_OFFSET_ROW = 6;
    private static final int STYLE_COUNT = 3;

    private ImageView imageView = null;
    private Field[][] fields = null;
    private Image snapshot = null;
    private Vector dimension = null;
    private int style = 0;

    public Vector getDimension() {
        return dimension;
    }

    public Field[][] getFields() {
        return fields;
    }

    public Field getField(int i, int g) {
        return fields[i][g];
    }

    public void setStyle(int style) {
        this.style = Math.min(STYLE_COUNT - 1, Math.max(0, style));
    }

    public int getStyle() {
        return style;
    }

    public Image generateTileImage(char type) {
        int normalized = ((int) type) - ((int) 'A');

        int row = normalized % 16;
        int col = (normalized - row) / 16;

        return new WritableImage(image.getPixelReader(),
                START_OFFSET_COL * 8 + 1 + row * 9,
                (style * 3 + col) * 9,
                8,
                8);
    }

    public void inflate(ImageView imageView) {
        this.imageView = imageView;

        imageView.setImage(snapshot);
    }

    public void generatePlaygroundSnapshot(Pane rootLayout) {
        generatePlaygroundSnapshot(rootLayout, Constants.TILE_DIMEN_DEFAULT);
    }

    public void generatePlaygroundSnapshot(Pane rootLayout, int tileDimension) {
        Vector dimensionScaled = dimension.multiply(tileDimension);

        Canvas canvas = new Canvas(dimensionScaled.getX(), dimensionScaled.getY());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for(int i = 0; i < dimension.getX(); i++) {
            for(int g = 0; g < dimension.getY(); g++) {
                Field field = fields[i][g];
                field.render(rootLayout);

                Vector vector = new Vector(i, g);
                Image image = generateTileImage(field.getType());

                graphicsContext.drawImage(image,
                        vector.getX() * tileDimension,
                        vector.getY() * tileDimension, tileDimension, tileDimension);
            }
        }

        snapshot = canvas.snapshot(null, null);

        setChanged();
        notifyObservers(snapshot);
    }

    public void generateFields(String path) {
        File file = new File(path);

        generateFields(file);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void generateFields(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            if(line == null)
                return;

            String[] dimensions = line.split(" ");
            if(dimensions.length != 2)
                return;

            int width = Integer.valueOf(dimensions[1]);
            int height = Integer.valueOf(dimensions[0]);

            dimension = new Vector(width, height);
            fields = new Field[width][height];

            int row = 0;
            while((line = bufferedReader.readLine()) != null) {
                for(int col = 0; col < line.length(); col++) {
                    Vector vector = new Vector(row, col);

                    char type = line.charAt(col);

                    switch(type) {
                        case 'm' : {
                            fields[row][col] = new Location(vector, 'm');
                            break;
                        }
                        case 'n' : {}
                        case 'o' : {}
                        case 'p' : {
                            fields[row][col] = new Point(vector, type);
                            break;
                        }
                        default : {
                            fields[row][col] = new Wall(vector, type);
                        }
                    }
                }

                row++;
            }
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }
}
