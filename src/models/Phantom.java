package models;

import controllers.Board;
import views.FieldView;

import java.util.*;

public class Phantom extends Spirit {

    public static final int PHANTOM_SCORE = 250;

    private List<Segment> shortestPath = new ArrayList<>();
    private float wobblePivot = 0f;
    private int shortestPathCount = Integer.MAX_VALUE;

    public Phantom(Vector vector) {
        super(vector);
    }

    @Override
    public String getDefaultImage() {
        return FieldView.PHANTOM_NORMAL;
    }

    private void resolveBerserkState(boolean berserk) {
        setChanged();

        if(berserk) {
            notifyObservers(FieldView.PHANTOM_SCARED);
        } else {
            notifyObservers(FieldView.PHANTOM_NORMAL);
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

                if(!segment.isThere(target)) {
                    getPath(paths, segment, target);
                }

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

        if(paths.size() > 15)
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

        Segment head = shortestPath.get(0);
        int size = shortestPath.size();

        Vector goal;
        if(size > 1) {
            goal = Segment.getTarget(head, shortestPath.get(1));
        } else {
            goal = target;
        }

        Vector position = getVector();
        if(head.getOrientation() == Segment.HORIZONTAL) {
            if(position.getX() == goal.getX()) {
                shortestPath.remove(head);

                return followTarget(target);
            } else if(position.getX() < goal.getX()) {
                return Vector.getDirection(2).getVector();
            } else {
                return Vector.getDirection(3).getVector();
            }
        } else {
            if(position.getY() == goal.getY()) {
                shortestPath.remove(head);

                return followTarget(target);
            } else if(position.getY() < goal.getY()) {
                return Vector.getDirection(0).getVector();
            } else {
                return Vector.getDirection(1).getVector();
            }
        }
    }

    @Override
    public void update(Board board) {
        Vector nextPos = followTarget(board.pacman.getVector());

        if(nextPos == null || (board.getPlayground().getField(getVector().add(nextPos)) instanceof Wall)) {
            Random random = new Random();

            int pos;
            do {
                pos = random.nextInt(4);

                nextPos = getVector().add(Vector.getDirection(pos).getVector());
            } while(board.getPlayground().getField(nextPos) instanceof Wall);
        } else {
            nextPos = getVector().add(nextPos);
        }

        Vector currentPosition = board.getPlayground().resolveBoundaries(nextPos);
        board.checkCollisionPhantom(this);

        setVector(currentPosition);

        setChanged();
        notifyObservers(currentPosition);
    }

    @Override
    public void wobble() {
        wobblePivot = (wobblePivot + 1.15f) % ((float) Math.PI);

        setChanged();
        notifyObservers((Math.cos(wobblePivot) * 5.0f));
    }
}
