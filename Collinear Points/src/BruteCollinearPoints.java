import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private int numLineSegments;
    private LineSegment[] lineSegments;

    private void addLineSegment(LineSegment l) {
        if(numLineSegments>=lineSegments.length-1) {
            LineSegment[] copy = new LineSegment[lineSegments.length * 2];
            System.arraycopy(lineSegments, 0, copy, 0, lineSegments.length);
            lineSegments = copy;
        }
        lineSegments[numLineSegments++]=l;
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        if(points==null) {
            throw new IllegalArgumentException("Points array is null");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point is null");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Two same points");
                }
            }
        }
        numLineSegments = 0;
        lineSegments = new LineSegment[2];

        boolean break_inner_loop;
        Point[] sameLinepoints = new Point[4];
        for(int i = 0; i < points.length; i++) {
            sameLinepoints[0] = points[i];
            for(int j = i + 1; j < points.length; j++) {
                break_inner_loop = false;
                sameLinepoints[1] = points[j];
                for(int k = j + 1; k < points.length; k++) {
                    sameLinepoints[2] = points[k];
                    for(int l = k + 1; l < points.length; l++) {
                        sameLinepoints[3] = points[l];
                        Comparator<Point> cmpi=points[i].slopeOrder();
                        Comparator<Point> cmpj=points[j].slopeOrder();
                        Comparator<Point> cmpk=points[k].slopeOrder();
                        // Comparator<Point> cmpl=points[l].slopeOrder();
                        if(cmpi.compare(points[j],points[k]) == 0 || cmpj.compare(points[i],points[k]) == 0){
                            if(cmpj.compare(points[k],points[l]) == 0 || cmpk.compare(points[j],points[l]) == 0) {
                                Arrays.sort(sameLinepoints, Point::compareTo);
                                addLineSegment(new LineSegment(sameLinepoints[0], sameLinepoints[sameLinepoints.length - 1]));
                                break_inner_loop = true;
                            }
                        }
                        if(break_inner_loop)
                            break;
                    }
                    if(break_inner_loop)
                        break;
                }
            }
        }
        LineSegment[] newLineSegment = new LineSegment[numLineSegments];
        System.arraycopy(lineSegments, 0, newLineSegment, 0, numLineSegments);
        lineSegments = newLineSegment;
    }

    // the number of line segments
    public int numberOfSegments() {
        return numLineSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
    }
}
