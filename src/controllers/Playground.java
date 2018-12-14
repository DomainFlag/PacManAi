package controllers;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import models.*;
import models.Point;
import core.Log;
import views.FieldView;

import java.io.*;
import java.util.Observable;

public class Playground extends Observable {

    public interface OnSelectPlaygroundField {
        void onSelectPlaygroundField(Field field);
    }

    private static final String path = "file:./../res/textures/texture.png";
    private static final Image image = new Image(path);

    public static final int MAX_DIMEN = 32;

    private static final int START_OFFSET_COL = 28;
    private static final int START_OFFSET_ROW = 6;
    private static final int STYLE_COUNT = 3;

    private File source = null;
    private Field[][] fields = null;
    private Image snapshot = null;
    private Vector dimension = null;
    private int total = 0;
    private int style = 0;

    public Vector getDimension() {
        return dimension;
    }

    public void setStyle(int style) {
        this.style = Math.min(STYLE_COUNT - 1, Math.max(0, style));
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Image getSnapshot() {
        return snapshot;
    }

    public void inflate(ImageView imageView) {
        imageView.setImage(snapshot);
    }

    public Field getField(Vector vector) {
        int x = vector.getX() < 0 ? dimension.getX() + vector.getX() : vector.getX() % dimension.getX();
        int y = vector.getY() < 0 ? dimension.getY() + vector.getY() : vector.getY() % dimension.getY();

        return fields[x][y];
    }

    public Vector resolveBoundaries(Vector vector) {
        int x = vector.getX() < 0 ? dimension.getX() - 1 : vector.getX() % dimension.getX();
        int y = vector.getY() < 0 ? dimension.getY() - 1: vector.getY() % dimension.getY();

        return new Vector(x, y);
    }

    public File getSource() {
        return source;
    }

    public boolean resolveDimension(int dimension) {
        return dimension <= MAX_DIMEN;
    }

    public void adjustPlayground(Vector dimen) {
        Vector topLeft = dimen, bottomRight = dimension;
        if(dimen.isGreater(dimension)) {
            topLeft = dimension;
            bottomRight = dimen;
        }

        for(int i = 0; i < topLeft.getX(); i++) {
            for(int g = topLeft.getY(); g < bottomRight.getY(); g++) {
                fields[i][g].resolve();
            }
        }

        for(int i = topLeft.getX(); i < bottomRight.getX(); i++) {
            for(int g = 0; g < bottomRight.getY(); g++) {
                fields[i][g].resolve();
            }
        }

        this.dimension = Vector.cloneVector(dimen);
    }

    public static Image generateTileImage(char type, int style) {
        int normalized = ((int) type) - ((int) 'A');

        int row = normalized % 16;
        int col = (normalized - row) / 16;

        return new WritableImage(image.getPixelReader(),
                START_OFFSET_COL * 8 + 1 + row * 9,
                (style * 3 + col) * 9,
                8,
                8);
    }

    public void generatePlaygroundCreator(@NotNull Pane rootLayout, OnSelectPlaygroundField onSelectPlaygroundField) {
        dimension = new Vector(MAX_DIMEN, MAX_DIMEN);
        fields = new Field[MAX_DIMEN][MAX_DIMEN];

        for(int i = 0; i < dimension.getX(); i++) {
            for(int g = 0; g < dimension.getY(); g++) {
                Vector position = new Vector(i, g);
                Field field = new Field(position, 'n');

                fields[i][g] = field;

                FieldView fieldView = new FieldView(field.getVector());
                fieldView.setImage(generateTileImage(field.getType(), 0));
                fieldView.inflate(rootLayout);
                fieldView.setOnMouseClicked(event -> {
                    onSelectPlaygroundField.onSelectPlaygroundField(field);
                });

                field.addObserver(fieldView);
            }
        }
    }

    public void generatePlaygroundSnapshot(@Nullable Pane rootLayout, int tileDimension) {
        Vector dimensionScaled = dimension.multiply(tileDimension);

        Canvas canvas = new Canvas(dimensionScaled.getX(), dimensionScaled.getY());
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        for(int i = 0; i < dimension.getX(); i++) {
            for(int g = 0; g < dimension.getY(); g++) {
                Field field = fields[i][g];
                if(rootLayout != null && field instanceof Point) {
                    Point point = (Point) field;

                    FieldView fieldView = new FieldView(field.getVector());
                    fieldView.setImage(generateTileImage(point.getFloatedType(), style));
                    fieldView.inflate(rootLayout);

                    field.addObserver(fieldView);
                }

                Vector vector = new Vector(i, g);
                Image image = Playground.generateTileImage(field.getType(), style);

                graphicsContext.drawImage(image,
                        vector.getX() * tileDimension,
                        vector.getY() * tileDimension, tileDimension, tileDimension);
            }
        }

        snapshot = canvas.snapshot(null, null);

        setChanged();
        notifyObservers(snapshot);
    }


    public void saveBoard(String filename) {
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

    public void fillPoints() {
        for(int g = 0; g < dimension.getX(); g++) {
            for(int h = 0; h < dimension.getY(); h++) {
                if(fields[g][h].getType() == Location.LOCATION_TYPE)
                    fields[g][h].resolve('n');
            }
        }
    }

    private String encodeBoard() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(dimension.getX());
        stringBuilder.append(" ");
        stringBuilder.append(dimension.getY());
        stringBuilder.append("\n");

        for(int i = 0; i < dimension.getX(); i++) {
            for(int g = 0; g < dimension.getY(); g++) {
                stringBuilder.append(fields[i][g].getType());
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public void generateFields(File file) {
        this.source = file;

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

            Vector dimen = new Vector(width, height);
            boolean activePlayground = dimension != null;

            if(dimension == null) {
                fields = new Field[width][height];
                dimension = dimen;
            } else {
                adjustPlayground(dimen);
            }

            int row = 0;
            while((line = bufferedReader.readLine()) != null) {
                for(int col = 0; col < line.length(); col++) {
                    Vector vector = new Vector(row, col);

                    char type = line.charAt(col);

                    if(activePlayground) {
                        fields[row][col].resolve(type);
                    } else {
                        switch(type) {
                            case Location.LOCATION_TYPE : {
                                fields[row][col] = new Location(vector, Location.LOCATION_TYPE);
                                break;
                            }
                            case 'n' : {}
                            case 'o' : {}
                            case 'p' : {
                                fields[row][col] = new Point(vector, type);

                                total++;
                                break;
                            }
                            default : {
                                fields[row][col] = new Wall(vector, type);
                            }
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
