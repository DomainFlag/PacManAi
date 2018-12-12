package models;

import com.sun.deploy.util.OrderedHashSet;
import controllers.Board;
import core.Segment;
import javafx.scene.image.Image;
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

    private double wobblePivot = 0.0f;

    private List<Segment> shortestPath = new ArrayList<>();
    private int shortestPathCount = Integer.MAX_VALUE;

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

    /**
     * Find the smallest path
     * @param graph our graph of all the connected segments on the map
     * @param target target
     */
    public void findShortestPath(HashMap<String, Segment> graph, Vector target) {
        shortestPathCount = Integer.MAX_VALUE;

        for(Segment segment : graph.values()) {
            if(segment.isThere(getVector())) {
                LinkedHashMap<String, Segment> paths = new LinkedHashMap<>();
                paths.put(segment.encodeSegment(), segment);

                getPath(paths, segment, target);

                break;
            }
        }
    }

    private void resolveShortestPath(LinkedHashMap<String, Segment> segments) {
        if(shortestPathCount > segments.size() && segments.size() != 0) {
            shortestPathCount = segments.size();

            shortestPath.clear();
            shortestPath.addAll(segments.values());
        }
    }

    private boolean getPath(LinkedHashMap<String, Segment> paths, Segment head, Vector target) {
        if(paths.isEmpty() || paths.size() > shortestPathCount)
            return false;

        for(Segment segment : head.getSegments()) {
            String encodedSegment = segment.encodeSegment();

            if(!segment.isThere(target)) {
                if(!paths.containsKey(encodedSegment)) {
                    paths.put(encodedSegment, segment);

                    if(getPath(paths, segment, target)) {
                        return true;
                    } else {
                        // Backtracking as we reached the max segments or we found a possible solution
                        paths.remove(encodedSegment);
                    }
                }
            } else {
                paths.put(encodedSegment, segment);

                resolveShortestPath(paths);

                // Backtracking for best solution
                paths.remove(encodedSegment);

                return false;
            }
        }

        return false;
    }

    private Vector followTarget(Vector target) {
        if(shortestPath.isEmpty()) {
            return null;
        }

        int size = shortestPath.size();
        Segment head = shortestPath.get(0);

        if(size > 1) {
            target = Segment.getTarget(head, shortestPath.get(1));
        } else return null;

        Vector position = getVector();
        if(head.getOrientation() == Segment.HORIZONTAL) {
            if(position.getX() == target.getX()) {
                shortestPath.remove(head);

                return followTarget(target);
            } else if(position.getX() < target.getX()) {
                return Vector.getDirection(2);
            } else {
                return Vector.getDirection(3);
            }
        } else {
            if(position.getY() == target.getY()) {
                shortestPath.remove(head);

                return followTarget(target);
            } else if(position.getY() < target.getY()) {
                return Vector.getDirection(0);
            } else {
                return Vector.getDirection(1);
            }
        }
    }

    @Override
    public void update(Board board) {
        Vector nextPos = followTarget(board.pacman.getVector());

        if(nextPos == null) {
            Random random = new Random();

            int pos;
            do {
                pos = random.nextInt(4);

                nextPos = getVector().add(Vector.getDirection(pos));
            } while(board.getPlayground().getField(nextPos) instanceof Wall);
        } else {
            nextPos = getVector().add(nextPos);
        }

        Vector currentPosition = board.getPlayground().resolveBoundaries(nextPos);
        board.checkCollisionPhantom(this);

        setVector(currentPosition);
        updateLayout();
    }

    @Override
    public void wobble() {
        double initialPosition = getImageView().getLayoutY() - Math.cos(wobblePivot) * 2.0f;

        wobblePivot = (wobblePivot + 1.15f) % (Math.PI * 2.0f);
        initialPosition += Math.cos(wobblePivot) * 2.0f;

        getImageView().setLayoutY(initialPosition);
    }
}
