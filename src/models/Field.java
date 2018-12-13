package models;

import java.util.Observable;

public class Field extends Observable {

    private Vector vector;
    private char type;

    public Field(Vector vector, char type) {
        this.type = type;
        this.vector = vector;
    }

    public char getType() {
        return type;
    }

    public Vector getVector() {
        return vector;
    }

    public void resolve(char type) {
        this.type = type;

        setChanged();
        notifyObservers(type);
    }

    public void resolve() {
        setChanged();
        notifyObservers(null);
    }
}
