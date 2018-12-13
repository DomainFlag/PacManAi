package models;

import controllers.Board;

import views.FieldView;

public class PacMan extends Spirit {

    private Vector direction = new Vector(1, 0);
    private int imagePosition = 0;

    public PacMan(Vector vector) {
        super(vector);
    }

    @Override
    public String getDefaultImage() {
        return FieldView.PACMAN_HUNGRY;
    }

    public void updatePosition(Direction direction) {
        this.direction = direction.getVector();

        setChanged();
        notifyObservers(direction.getRotation());
    }

    @Override
    public void wobble() {
        imagePosition = (imagePosition + 1) % 2;

        String imageToBeUpdated;
        if(imagePosition == 0)
            imageToBeUpdated = FieldView.PACMAN_FED;
        else imageToBeUpdated = FieldView.PACMAN_HUNGRY;

        setChanged();
        notifyObservers(imageToBeUpdated);
    }

    @Override
    public void update(Board board) {
        Vector position = getVector().add(direction);
        if(!(board.getPlayground().getField(position) instanceof Wall)) {
            Vector currentPosition = board.getPlayground().resolveBoundaries(position);

            board.checkCollisionPhantoms();
            board.checkCollisionPoints(currentPosition);

            setVector(currentPosition);

            setChanged();
            notifyObservers(currentPosition);
        }

        wobble();
    }
}
