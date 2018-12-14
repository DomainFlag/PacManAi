package views;

import controllers.Playground;
import core.Constants;
import interfaces.Inflater;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import models.Vector;

import java.util.*;

public class FieldView extends ImageView implements Inflater, Observer {

    private static final HashMap<String, Image> images = new HashMap<>();

    public static final String PACMAN_HUNGRY = "file:./../res/textures/pacman-hungry.png";
    public static final String PACMAN_FED = "file:./../res/textures/pacman-fed.png";
    public static final String PHANTOM_NORMAL = "file:./../res/textures/ghost-normal.png";
    public static final String PHANTOM_SCARED = "file:./../res/textures/ghost-scared.png";

    static {
        images.put(PACMAN_HUNGRY, new Image(PACMAN_HUNGRY));
        images.put(PACMAN_FED, new Image(PACMAN_FED));
        images.put(PHANTOM_NORMAL, new Image(PHANTOM_NORMAL));
        images.put(PHANTOM_SCARED, new Image(PHANTOM_SCARED));
    }

    private Pane root = null;

    public FieldView() {
        setFitWidth(Constants.TILE_DIMEN_DEFAULT);
        setFitHeight(Constants.TILE_DIMEN_DEFAULT);
    }

    public FieldView(Vector vector) {
        this();

        changeLayout(vector);
    }

    public void inflateImage(String image) {
        setImage(images.get(image));
    }

    public void changeLayout(Vector vector) {
        setLayoutX(vector.getX() * Constants.TILE_DIMEN_DEFAULT);
        setLayoutY(vector.getY() * Constants.TILE_DIMEN_DEFAULT);
    }

    @Override
    public void inflate(Pane pane) {
        if(pane != null)
            root = pane;

        if(root != null) {
            if(root.getChildren().contains(this))
                root.getChildren().remove(this);
            else root.getChildren().add(this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg == null) {
            inflate(null);
        } else if(arg instanceof Vector) {
            changeLayout((Vector) arg);
        } else if(arg instanceof Boolean) {
            setVisible((Boolean) arg);
        } else if(arg instanceof Image) {
            setImage((Image) arg);
        } else if(arg instanceof String) {
            setImage(images.get(arg));
        } else if(arg instanceof Double) {
            setRotate((Double) arg);
        } else if(arg instanceof Character) {
            setImage(Playground.generateTileImage((Character) arg, 0));
        }
    }
}
