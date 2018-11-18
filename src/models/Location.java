package models;

public class Location extends Field {

    public static final int LOC_TYPE = 'p';

    public int type;

    public Location(Vector vector, char type) {
        super(vector, type);
    }

    @Override
    public void update() {}
}
