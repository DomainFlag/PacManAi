package models;

import java.util.HashMap;

public class Vector {

    public static final HashMap<Integer, Vector> directions = new HashMap<>();

    static {
        directions.put(0, new Vector(0, 1));
        directions.put(1, new Vector(0, -1));
        directions.put(2, new Vector(1, 0));
        directions.put(3, new Vector(-1, 0));
    }

    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(int x, int y) {
        return new Vector(this.x + x, this.y + y);
    }

    public Vector add(Vector vector) {
        return add(vector.getX(), vector.getY());
    }

    public static Vector getDirection(int pos) {
        return directions.get(pos);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
}
