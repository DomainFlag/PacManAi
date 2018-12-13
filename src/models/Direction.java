package models;

public class Direction {

    private Vector vector;
    private double rotation;

    Direction(Vector vector, double rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    public Vector getVector() {
        return vector;
    }

    public double getRotation() {
        return rotation;
    }
}
