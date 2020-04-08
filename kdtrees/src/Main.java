import edu.princeton.cs.algs4.*;

import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String filename = args[0];

        In in = new In(filename);
        KdTree kdt = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdt.insert(p);
        }
        /*
        Iterable<Point2D> it = kdt.allPoints();
        for (Point2D p : it) {
            StdOut.println(p);
        }

         */
        kdt.draw();
        System.out.println(kdt.contains(new Point2D(0.320, 0.708)));
        Iterable<Point2D> itrange = kdt.range(new RectHV(0, 0, 0.5, 0.5));
        for (Point2D p : itrange) {
            StdOut.println(p);
        }
        Point2D near = kdt.nearest(new Point2D(0.0, 0.0));
        System.out.println("Near Point:" + near);
        System.out.println("End");
        /*
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        RectHV rc = new RectHV(0.1, 0.1, 0.5, 0.5);
        System.out.println(rc);
        /*
        Iterable<Point2D> s = brute.allPoints();
        for (Point2D p : s) {
            System.out.println("All points" + p);
        }


        brute.draw();
        Iterable<Point2D> s1 = brute.range(rc);
        for (Point2D p : s1) {
            System.out.println("Points" + p);
        }
        Point2D x = new Point2D(0.5, 0.5);
        Point2D nearP = brute.nearest(x);
        System.out.println("Nearest point to point" + x + "is" + nearP);
        */
    }
}
