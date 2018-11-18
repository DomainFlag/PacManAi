package models;

public class Wall extends Field {

    public char type;

    public Wall(Vector vector, char type) {
        super(vector, type);

        this.type = type;
    }

    @Override
    public void update() {

    }
}
