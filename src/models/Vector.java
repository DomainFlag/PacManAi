package models;

import core.Segment;

import java.util.HashMap;

public class Vector {

    public static final HashMap<Integer, Vector> directions = new HashMap<>();

    static {
        directions.put(0, new Vector(0, 1));
        directions.put(1, new Vector(0, -1));
        directions.put(2, new Vector(1, 0));
        directions.put(3, new Vector(-1, 0));
    }

    public int x;
    public int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector() { }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector) {
            Vector vector = (Vector) obj;

            return vector.getX() == getX() && vector.getY() == getY();
        }

        return false;
    }

    @Override
    public String toString() {
        return "Vector(" + getX() + ", " + getY() + ")";
    }

    public Vector add(int x, int y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector add(Vector vector) {
        return add(vector.getX(), vector.getY());
    }

    public Vector subtract(Vector vector) {
        return new Vector(this.x - vector.getX(), this.y  + vector.getY());
    }

    public Vector abs() {
        this.x = Math.abs(x);
        this.y = Math.abs(y);

        return this;
    }

    public Vector multiply(int scalar) {
        return new Vector(getX() * scalar, getY() * scalar);
    }

    public static Vector getDirection(int pos) {
        return directions.get(pos);
    }

    public boolean isGreater(Vector vector) {
        return getX() >= vector.getX() && getY() >= vector.getY();
    }

    public int getMax() {
        return Math.max(getX(), getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOrientedValue(int orientation) {
        if(orientation == Segment.HORIZONTAL)
            return getX();
        else return getY();
    }

    public int getOrientedPivot(int orientation) {
        if(orientation == Segment.HORIZONTAL)
            return getY();
        else return getX();
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
