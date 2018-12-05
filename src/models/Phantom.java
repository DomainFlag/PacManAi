package models;

import controllers.Board;
import core.Constants;
import core.Segment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import tools.Log;

import java.util.*;

public class Phantom extends Spirit {

    public static final int PHANTOM_SCORE = 250;

    private static final List<String> paths = new ArrayList<>();

    static {
        paths.add("file:./../res/textures/ghost-normal.png");
        paths.add("file:./../res/textures/ghost-scared.png");
    }

    private static final List<Image> images = new ArrayList<>();

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

    public void findPath(Board board, PacMan pacMan) {
        List<Segment> graph = board.getGraph();

        Vector target = pacMan.getVector();

        for(Segment segment : graph) {
            if(segment.isThere(getVector())) {
                List<Segment> beenThere = new ArrayList<>();
                beenThere.add(segment);

                List<Segment> paths = new ArrayList<>();
                paths.add(segment);

                getPath(beenThere, paths, pacMan.getVector());
                Log.v(paths.size());
                Log.v(paths.get(paths.size() - 1).toString());

                break;
            }
        }
    }

    public boolean getPath(List<Segment> beenThere, List<Segment> paths, Vector target) {
        // TODO(0) needs to be done
        for(Segment segment : paths.get(paths.size() - 1).getSegments()) {
            if(!segment.isThere(target)) {
                Segment currSegment = Segment.resolveSegment(beenThere, segment);

                if(currSegment == segment) {
                    paths.add(segment);
                    beenThere.add(segment);

                    if(getPath(beenThere, paths, target)) {
                        return true;
                    } else {
                        paths.remove(segment);
                        beenThere.remove(segment);
                    }
                }
            } else {
                paths.add(segment);
                beenThere.add(segment);

                return true;
            }
        }

        return false;
    }

    @Override
    public void update(Board board) {
        Random random = new Random();

        Vector nextPos;
        int pos;
        do {
            pos = random.nextInt(4);

            nextPos = getVector().add(Vector.getDirection(pos));
        } while(board.getField(nextPos) instanceof Wall);

        Vector currentPosition = board.resolveBoundaries(nextPos);
        board.checkCollisionPhantom(this);

        setVector(currentPosition);
        updateLayout();
    }
}
