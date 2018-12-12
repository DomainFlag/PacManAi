package core;

import controllers.Board;
import controllers.Playground;
import models.Vector;
import models.Wall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Segment {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private static final List<List<Vector>> orientations = new ArrayList<>();

    static {
        List<Vector> horizontal = new ArrayList<>();
        horizontal.add(Vector.getDirection(2));
        horizontal.add(Vector.getDirection(3));

        List<Vector> vertical = new ArrayList<>();
        vertical.add(Vector.getDirection(0));
        vertical.add(Vector.getDirection(1));

        orientations.add(horizontal);
        orientations.add(vertical);
    }

    private List<Segment> segments = new ArrayList<>();
    private Vector range;
    private int pivot;
    private int orientation;

    public Segment(Vector range, int pivot, int orientation) {
        this.range = range;
        this.pivot = pivot;
        this.orientation = orientation;
    }

    public static Vector getTarget(Segment head, Segment tail) {
        if(head.orientation == HORIZONTAL) {
            return new Vector(tail.pivot, head.pivot);
        } else {
            return new Vector(head.pivot, tail.pivot);
        }
    }

    public int getPivot() {
        return pivot;
    }

    public int getOrientation() {
        return orientation;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void add(Segment segment) {
        segments.add(segment);
    }

    public boolean isThere(Vector vector) {
        if(orientation == Segment.HORIZONTAL) {
            return pivot == vector.getY() &&
                    (range.getX() <= vector.getX() && vector.getX() <= range.getY() ||
                            range.getY() <= vector.getX() &&  vector.getX() <= range.getX());
        } else {
            return pivot == vector.getX() &&
                    (range.getY() <= vector.getY() && vector.getY() <= range.getX() ||
                            range.getX() <= vector.getY() &&  vector.getY() <= range.getY());
        }
    }

    public String encodeSegment() {
        return String.valueOf(orientation) +
                pivot +
                range.toString();
    }

    public static Segment resolvePosition(HashMap<String, Segment> graph, Playground playground, Vector vector, int orientation) {
        List<Vector> segmentsToVisit = new ArrayList<>();

        int nextOrientation = (orientation + 1) % 2;
        int rangeStart = vector.getOrientedValue(orientation), rangeEnd = rangeStart;
        int pivot = vector.getOrientedPivot(orientation);

        Vector prov = new Vector(vector.getX(), vector.getY());
        List<Vector> directions = orientations.get(orientation);

        getOppositeSegment(segmentsToVisit, playground, prov, nextOrientation);
        for(Vector direction : directions) {
            while(!(playground.getField(prov = prov.add(direction)) instanceof Wall)) {
                int current = prov.getOrientedValue(orientation);

                rangeStart = Math.min(rangeStart, current);
                rangeEnd = Math.max(rangeEnd, current);

                getOppositeSegment(segmentsToVisit, playground, prov, nextOrientation);
            }

            prov = new Vector(vector.getX(), vector.getY());
        }

        Segment parentSegment = new Segment(new Vector(rangeStart, rangeEnd), pivot, orientation);
        String encodedSegment = parentSegment.encodeSegment();
        if(!graph.containsKey(encodedSegment)) {
            graph.put(encodedSegment, parentSegment);

            for(Vector segmentToVisit : segmentsToVisit) {
                Segment childSegment = resolvePosition(graph, playground, segmentToVisit, nextOrientation);

                parentSegment.add(childSegment);
            }
        } else {
            // Crucial one, parentSegment it's a new instance, thus if graph contains it and we are not retrieving it
            // then we won't be able to make a linked graph of segments.
            parentSegment = graph.get(encodedSegment);
        }

        return parentSegment;
    }

    private static void getOppositeSegment(List<Vector> segmentsToVisit, Playground playground, Vector prov, int orientation) {
        List<Vector> directions = orientations.get(orientation);

        if(!(playground.getField(prov.add(directions.get(0))) instanceof Wall) ||
                !(playground.getField(prov.add(directions.get(1))) instanceof Wall)) {
            segmentsToVisit.add(playground.resolveBoundaries(prov));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Segment) {
            Segment segment = (Segment) obj;

            return pivot == segment.pivot && orientation == segment.orientation
                    && (range.equals(segment.range) || (range.getY() == segment.range.getX() &&
                    range.getX() == segment.range.getY()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Orientation: " + orientation + "; Pivot: " + pivot + "; Range: " + range.toString();
    }
}