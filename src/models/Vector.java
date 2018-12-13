package models;

import java.util.ArrayList;
import java.util.List;

public class Vector {

    private static final List<Direction> directions = new ArrayList<>();

    static {
        directions.add(new Direction(new Vector(0, 1), 90d));
        directions.add(new Direction(new Vector(0, -1), 270d));
        directions.add(new Direction(new Vector(1, 0), 0d));
        directions.add(new Direction(new Vector(-1, 0), 180d));
    }

    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector() { }

    public Vector add(int x, int y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector add(Vector vector) {
        return add(vector.getX(), vector.getY());
    }

    public Vector multiply(int scalar) {
        return new Vector(getX() * scalar, getY() * scalar);
    }

    public boolean isGreater(Vector vector) {
        return getX() >= vector.getX() && getY() >= vector.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    int getOrientedValue(int orientation) {
        if(orientation == Segment.HORIZONTAL)
            return getX();
        else return getY();
    }

    int getOrientedPivot(int orientation) {
        if(orientation == Segment.HORIZONTAL)
            return getY();
        else return getX();
    }

    public static Vector cloneVector(Vector vector) {
        return new Vector(vector.getX(), vector.getY());
    }

    public static Direction getDirection(int pos) {
        return directions.get(pos);
    }

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
        return "Vector(" + getX() + "; " + getY() + ")";
    }
}
