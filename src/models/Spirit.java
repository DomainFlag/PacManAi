package models;

import controllers.Board;
import java.util.Observable;

public abstract class Spirit extends Observable {

    private Vector vector;
    public boolean active = true;

    public Spirit(Vector vector) {
        this.vector = vector;
    }

    public void disable() {
        setChanged();
        notifyObservers(false);

        active = false;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public void updateSpirit(Board board) {
        if(active)
            update(board);
    }

    public abstract String getDefaultImage();

    public abstract void update(Board board);

    public abstract void wobble();
}
