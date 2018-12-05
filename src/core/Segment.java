package core;

import controllers.Board;
import models.Vector;
import models.Wall;

import java.util.ArrayList;
import java.util.List;

public class Segment {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private List<Segment> segments = new ArrayList<>();
    private Vector start;
    private Vector end;
    private int orientation;
    private int cost;

    public Segment(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void add(Segment segment) {
        segments.add(segment);
    }

    public int getCost() {
        return cost;
    }

    public Vector getEnd() {
        return end;
    }

    public Vector getStart() {
        return start;
    }

    public boolean isThere(Vector vector) {
        if(orientation == Segment.HORIZONTAL) {
            return start.getY() == vector.getY() &&
                    ((start.getX() <= vector.getX() && vector.getX() <= end.getX()) ||
                            ((end.getX() <= vector.getX() && vector.getX() <= start.getX())));
        } else {
            return start.getX() == vector.getX() &&
                    ((start.getY() <= vector.getY() && vector.getY() <= end.getY()) ||
                            ((end.getY() <= vector.getY() && vector.getY() <= start.getY())));
        }
    }

    public static Segment resolveSegment(List<Segment> segments, Segment segment) {
        for(Segment graphSegment : segments) {
            if(graphSegment.equals(segment)) {
                return graphSegment;
            }
        }

        return segment;
    }

    public static Segment resolvePosition(List<Segment> graph, Board board, Vector vector, int orientation) {
        List<Vector> segmentsToBeVisited = new ArrayList<>();

        int minX = vector.getX(), maxX = minX;
        int minY = vector.getY(), maxY = minY;

        Vector prov;
        if(orientation == Segment.HORIZONTAL) {
            prov = new Vector(vector.getX(), vector.getY());
            getFollowingVerticalSegment(segmentsToBeVisited, board, prov);

            while(!(board.getField(prov = prov.add(1, 0)) instanceof Wall)) {
                maxX = prov.getX();

                getFollowingVerticalSegment(segmentsToBeVisited, board, prov);
            }

            prov = new Vector(vector.getX(), vector.getY());
            while(!(board.getField(prov = prov.add(-1, 0)) instanceof Wall)) {
                minX = prov.getX();

                getFollowingVerticalSegment(segmentsToBeVisited, board, prov);
            }
        } else {
            prov = new Vector(vector.getX(), vector.getY());
            getFollowingHorizontalSegment(segmentsToBeVisited, board, prov);

            while(!(board.getField(prov = prov.add(0, 1)) instanceof Wall)) {
                minY = prov.getY();

                getFollowingHorizontalSegment(segmentsToBeVisited, board, prov);
            }

            prov = new Vector(vector.getX(), vector.getY());
            while(!(board.getField(prov = prov.add(0, -1)) instanceof Wall)) {
                maxY = prov.getY();

                getFollowingHorizontalSegment(segmentsToBeVisited, board, prov);
            }
        }

        Segment segment = new Segment(new Vector(minX, minY), new Vector(maxX, maxY));
        segment.setOrientation(orientation);

        Segment currentSegment = Segment.resolveSegment(graph, segment);
        if(segment == currentSegment) {
            graph.add(segment);

            for(Vector segmentPositionToBeVisited : segmentsToBeVisited) {
                Segment auxiliarySegment = resolvePosition(graph, board, segmentPositionToBeVisited, (orientation + 1) % 2);
                segment.add(auxiliarySegment);
            }
        }

        return currentSegment;
    }

    private static void getFollowingVerticalSegment(List<Vector> segmentsToBeVisited, Board board, Vector prov) {
        if(!(board.getField(prov.add(0, 1)) instanceof Wall) ||
                !(board.getField(prov.add(0, -1)) instanceof Wall)) {
            segmentsToBeVisited.add(board.resolveBoundaries(prov));
        }
    }

    private static void getFollowingHorizontalSegment(List<Vector> segmentsToBeVisited, Board board, Vector prov) {
        if(!(board.getField(prov.add(1, 0)) instanceof Wall) ||
                !(board.getField(prov.add(-1, 0)) instanceof Wall)) {
            segmentsToBeVisited.add(board.resolveBoundaries(prov));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Segment) {
            Segment segment = (Segment) obj;

            return start.equals(segment.getStart()) && end.equals(segment.getEnd()) ||
                    end.equals(segment.getStart()) && start.equals(segment.getEnd());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Start: " + getStart().toString() + " End: " + getEnd().toString();
    }
}
