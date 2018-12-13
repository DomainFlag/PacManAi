package models;

public class Point extends Field {

    private char floatedType;
    private int value;

    public boolean powerPoint;

    public Point(Vector vector, char type) {
        super(vector, 'm');

        floatedType = type;
        value = (int) Math.pow(10, floatedType - 'n' + 1);
        powerPoint = value > 10;
    }

    public char getFloatedType() {
        return floatedType;
    }

    public void disable() {
        setChanged();
        notifyObservers(false);

        value = 0;
    }

    public int getValue() {
        return value;
    }
}
