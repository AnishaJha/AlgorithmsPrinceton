import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class FastCollinearPoints {
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

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if(points == null) {
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
        double[] slope = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if(i==j)
                    continue;
                slope[j] = points[i].slopeTo(points[j]);
            }
            sortPoints(i+1, slope, points);
            Arrays.sort(slope,0,i);

            for (int j = i + 1; j < points.length-2; ) {
                int k = j + 1;
                int count = 0;
                Point min_point;
                Point max_point;
                if(points[i].compareTo(points[j]) < 0) {
                    min_point = points[i];
                    max_point = points[j];
                }
                else {
                    min_point = points[j];
                    max_point = points[i];
                }

                while(k<points.length && (slope[j] == slope[k])) {
                    if (points[k].compareTo(min_point) < 0)
                        min_point = points[k];
                    if (points[k].compareTo(max_point) > 0)
                        max_point = points[k];
                    count = count + 1;
                    k = k + 1;
                }

                if (count >= 2) {
                    if (i == 0) {
                        addLineSegment(new LineSegment(min_point, max_point));
                    }
                    else if (Arrays.binarySearch(slope, 0, i, slope[j]) < 0) {
                        addLineSegment(new LineSegment(min_point, max_point));
                    }
                }
                j=k;
            }
        }
        LineSegment[] newLineSegment = new LineSegment[numLineSegments];
        System.arraycopy(lineSegments, 0, newLineSegment, 0, numLineSegments);
        lineSegments = newLineSegment;
    }

    private void sortPoints(int stIndex, double[] slope, Point[] points) {

        quickSort(stIndex, slope.length-1, slope, points);
        assert isSorted(slope, stIndex, slope.length-1);
    }

    private void quickSort(int lo, int hi, double[] slope, Point[] points) {
        if (hi <= lo) return;
        int j = partition(lo, hi, slope, points);
        quickSort(lo, j-1, slope, points);
        quickSort(j+1, hi, slope, points);
        assert isSorted(slope, lo, hi);
    }

    private int partition(int lo, int hi, double[] slope, Point[] points) {
        int i = lo;
        int j = hi + 1;
        int index = StdRandom.uniform(lo,hi+1);
        exch(lo, index, slope, points);
        double v = slope[lo];
        while (true) {
            while (less(slope[++i], v) )
                if (i == hi) break;

            while (less(v, slope[--j]) )
                if (j == lo) break;
            if (i >= j) break;
            exch(i, j, slope, points);
        }
        exch(lo, j, slope, points);
        return j;
    }

    private void exch(int i, int j, double[] slope, Point[] points){
        double swp1 = slope[i];
        slope[i] = slope [j];
        slope[j] = swp1;
        Point swp2 = points[i];
        points[i] = points[j];
        points[j] = swp2;
    }

    private static boolean less(double slp1, double slp2){
        return Double.compare(slp1,slp2) == -1;

    }
    private static boolean isSorted(double[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
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
