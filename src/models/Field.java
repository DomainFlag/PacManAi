package models;

import java.util.Observable;

public abstract class Field extends Observable {

    private Vector vector;

    private char type;

    public Field(Vector vector, char type) {
        this.type = type;
        this.vector = vector;
    }

    public abstract void update();

    public char getType() {
        return type;
    }
}
