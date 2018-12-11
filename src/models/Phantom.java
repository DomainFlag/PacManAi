package models;

import controllers.Board;
import core.Segment;
import javafx.scene.image.Image;
import java.util.*;

public class Phantom extends Spirit {

    public static final int PHANTOM_SCORE = 250;

    private static final List<String> paths = new ArrayList<>();

    static {
        paths.add("file:./../res/textures/ghost-normal.png");
        paths.add("file:./../res/textures/ghost-scared.png");
    }

    private static final List<Image> images = new ArrayList<>();

    private int min = Integer.MAX_VALUE;
    private List<Segment> minS = new ArrayList<>();

    static {
        for(String path : paths) {
            images.add(new Image(path));
        }
    }

    public Phantom(Vector vector) {
        super(vector, images);
    }

    public void resolveBerserkState(boolean berserk) {
        if(berserk) {
            getImageView().setImage(images.get(1));
        } else {
            getImageView().setImage(images.get(0));
        }
    }

    public static void resolveBerserkState(List<Phantom> phantoms, boolean berserk) {
        for(Phantom phantom : phantoms)
            phantom.resolveBerserkState(berserk);
    }

    public static boolean resolvePhantoms(List<Phantom> phantoms) {
        for(Phantom phantom : phantoms) {
            if(phantom.active)
                return false;
        }

        return true;
    }

    public void min(List<Segment> segments) {
        if(min > segments.size()) {
            min = segments.size();

            minS.clear();
            minS.addAll(segments);
        }
    }

    public void findPath(Board board, PacMan pacMan) {
        min = Integer.MAX_VALUE;

        List<Segment> graph = board.getGraph();

        for(Segment segment : graph) {
            if(segment.isThere(getVector())) {
                List<Segment> paths = new ArrayList<>();
                paths.add(segment);

                getPath(paths, pacMan.getVector());

                break;
            }
        }
    }

    public boolean getPath(List<Segment> paths, Vector target) {
        if(paths.isEmpty())
            return false;

        if(paths.size() > min)
            return false;

        // TODO(0) needs to be done
        for(Segment segment : paths.get(paths.size() - 1).getSegments()) {
            if(!segment.isThere(target)) {
                boolean wasThere = false;
                for(Segment seg : paths) {
                    if(seg == segment)
                        wasThere = true;
                }

                if(!wasThere) {
                    paths.add(segment);

                    if(getPath(paths, target)) {
                        return true;
                    } else {
                        paths.remove(segment);
                    }
                }
            } else {
                paths.add(segment);

                min(paths);

                paths.remove(segment);

                return false;
            }
        }

        return false;
    }

    public Vector follow(List<Segment> segments, Vector pacman) {
        if(segments.isEmpty())
            return null;

        Vector target;
        if(segments.size() <= 1)
            target = pacman;
        else target = segments.get(1).getStart();

        Segment segment = segments.get(0);

        Vector position = getVector();
        if(segment.orientation == Segment.HORIZONTAL) {
            if(position.getX() == target.getX()) {
                segments.remove(0);

                return follow(segments, pacman);
            } else if(position.getX() < target.getX()) {
                return Vector.getDirection(2);
            } else {
                return Vector.getDirection(3);
            }
        } else {
            if(position.getY() == target.getY()) {
                segments.remove(0);

                return follow(segments, pacman);
            } else if(position.getY() < target.getY()) {
                return Vector.getDirection(0);
            } else {
                return Vector.getDirection(1);
            }
        }
    }

    @Override
    public void update(Board board) {
        Vector nextPos;
        if(minS.size() == 0 || ((nextPos = follow(minS, board.pacman.getVector())) == null)) {
            Random random = new Random();

            int pos;
            do {
                pos = random.nextInt(4);

                nextPos = getVector().add(Vector.getDirection(pos));
            } while(board.getField(nextPos) instanceof Wall);
        } else {
            if(nextPos == null)
                return;

            nextPos = getVector().add(nextPos);
        }

        Vector currentPosition = board.resolveBoundaries(nextPos);
        board.checkCollisionPhantom(this);

        setVector(currentPosition);
        updateLayout();
    }
}
