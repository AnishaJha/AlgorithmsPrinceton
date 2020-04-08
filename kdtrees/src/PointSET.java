import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> point2DS;
    private int n;

    // construct an empty set of points
    public PointSET()   {
        point2DS = new SET<Point2D>();
        n = 0;
    }
    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size() {
        return n;

    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Insert point is null");
        if (this.contains(p))
            return;
        point2DS.add(p);
        n++;
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Input point is null");
        return point2DS.contains(p);

    }
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        for (Point2D p : point2DS)
            StdDraw.point(p.x(), p.y());
        StdDraw.setPenRadius();
    }
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (point2DS.isEmpty())
            return null;
        if (rect == null)
            throw new IllegalArgumentException("Input rectangle is null");
        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        SET<Point2D> rset = new SET<>();
        for (Point2D p : point2DS) {
            if (p.x() >= xmin && p.y() >= ymin && p.x() <= xmax && p.y() <= ymax)
                rset.add(p);
        }
        return rset;
        /*
        Point2D min = new Point2D(xmin, ymin);
        Point2D max = new Point2D(xmax, ymax);
        Point2D floor = point2DS.floor(min);
        Point2D ceil = point2DS.ceiling(max);
        System.out.println("Floor of" + min + "is:" + floor);
        System.out.println("Ceil of" + max + "is:" + ceil);
        return new Point2DIterable();
         */
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)    {
        if (point2DS.isEmpty())
            return null;
        if ( p == null)
            throw new IllegalArgumentException("Input point is null");
        double dist = 10.0;
        Point2D pmin = null;
        for (Point2D pcurr : point2DS) {
            double sq_distance = pcurr.distanceSquaredTo(p);
            if (sq_distance < dist) {
                dist = sq_distance;
                pmin = pcurr;
            }

        }
        return pmin;
    }

    private Iterable<Point2D> allPoints() {
        return point2DS;
    }

    /*
    private class Point2DIterable implements Iterable<Point2D> {
        SET<Point2D> pointSet;
        public Point2DIterable(SET<Point2D> pointSet) {
            this.pointSet = pointSet;
        }
        public Iterator<Point2D> iterator() {
            return pointSet.iterator();
        }
    }

     */

    public static void main(String[] args) {

    }
}
