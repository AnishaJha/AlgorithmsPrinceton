import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class KdTree {
    private int n;
    private Node root;
    private Point2D findNearP;

    // construct an empty set of points
    public KdTree() {
        root = null;
        n = 0;
    }

    private class Node {
        private Point2D point;
        private Node left, right;

        public Node(Point2D point, Node left, Node right) {
            this.point = point;
            this.left = left;
            this.right = right;
        }
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
        Node newNode = new Node(p, null, null);
        if (isEmpty()) {
            root = newNode;
            n++;
            return;
        }
        if (this.contains(p))
            return;
        Node curr = root;
        boolean isX = true;
        int cmp;
        while (true) {
            if (isX)
                cmp = Point2D.X_ORDER.compare(p, curr.point);
            else
                cmp = Point2D.Y_ORDER.compare(p, curr.point);
            if (cmp < 0) {
                if (curr.left == null) {
                    curr.left = newNode;
                    break;
                }
                curr = curr.left;
            }
            else {
                if (curr.right == null) {
                    curr.right = newNode;
                    break;
                }
                curr = curr.right;
            }
            isX = !isX;
        }
        n++;
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Input point is null");
        if (isEmpty())
            return false;
        boolean isX = true;
        Node curr = root;
        int cmp;
        while (true) {
            if (curr.point.equals(p))
                return true;
            if (isX)
                cmp = Point2D.X_ORDER.compare(p, curr.point);
            else
                cmp = Point2D.Y_ORDER.compare(p, curr.point);
            if (cmp < 0) {
                if (curr.left == null)
                    return false;
                curr = curr.left;
            }
            else {
                if (curr.right == null)
                    return false;
                curr = curr.right;
            }
            isX = !isX;
        }
    }
    // draw all points to standard draw
    public void draw() {
        Node curr = root;
        RectHV rect = new RectHV(0, 0, 1, 1);
        print_ele(curr, true, rect);
        StdDraw.setPenRadius();
    }

    private void print_ele(Node curr, boolean isX, RectHV rect) {
        if (curr == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(curr.point.x(), curr.point.y());
        if (isX) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(curr.point.x(), rect.ymin(), curr.point.x(), rect.ymax());
            print_ele(curr.left, false, new RectHV(rect.xmin(), rect.ymin(), curr.point.x(), rect.ymax()));
            print_ele(curr.right, false, new RectHV(curr.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.002);
            StdDraw.line(rect.xmin(), curr.point.y(), rect.xmax(), curr.point.y());
            print_ele(curr.left, true, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), curr.point.y()));
            print_ele(curr.right, true, new RectHV(rect.xmin(), curr.point.y(), rect.xmax(), rect.ymax()));
        }
    }
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (isEmpty())
            return null;
        IntersectingPoints ip = new IntersectingPoints(rect);
        return new KdTreeIterable(ip.intersecting, ip.lenIntersecting);
    }

    private class IntersectingPoints {
        Node[] intersecting;
        int lenIntersecting;
        RectHV rect;
        public IntersectingPoints(RectHV rect) {
            intersecting = new Node[n];
            lenIntersecting = 0;
            this.rect = rect;
            findIntersecting(root, new RectHV(0, 0, 1, 1), true);
        }
        private void findIntersecting(Node curr, RectHV container, boolean Isx) {
            if (rect.contains(curr.point))
                intersecting[lenIntersecting++] = curr;
            if (Isx) {
                if (curr.left != null) {
                    RectHV leftRect = new RectHV(container.xmin(), container.ymin(), curr.point.x(), container.ymax());
                    if (leftRect.intersects(rect))
                        findIntersecting(curr.left, leftRect, false);
                }
                if (curr.right != null) {
                    RectHV rightRect = new RectHV(curr.point.x(), container.ymin(), container.xmax(), container.ymax());
                    if (rightRect.intersects(rect))
                        findIntersecting(curr.right, rightRect, false);
                }
            }
            else {
                if (curr.left != null) {
                    RectHV leftRect = new RectHV(container.xmin(), container.ymin(), container.xmax(), curr.point.y());
                    if (leftRect.intersects(rect))
                        findIntersecting(curr.left, leftRect, true);
                }
                if (curr.right != null) {
                    RectHV rightRect = new RectHV(container.xmin(), curr.point.y(), container.xmax(), container.ymax());
                    if (rightRect.intersects(rect))
                        findIntersecting(curr.right, rightRect, true);
                }
            }
        }
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Input point is null");
        if (root == null)
            return null;
        findNearP = p;
        double dist = findNearP.distanceSquaredTo(root.point);
        RectHV rect = new RectHV(0, 0, 1, 1);
        PointDist res = findNearest(root, rect, new PointDist(root.point, dist), true);
        return res.point;
    }

    private PointDist findNearest(Node curr, RectHV rect, PointDist minP, boolean Isx) {
        /*
        if (curr == null)
            return pointDist;


        if (pointDist.dist < rect.distanceSquaredTo(findNearP))
            return pointDist;

         */
        double currDist;
        PointDist p1;
        PointDist p2;
        // double currDist = findNearP.distanceSquaredTo(curr.point);
        /*
        if (currDist < pointDist.dist)
            minP = new PointDist(curr.point, currDist);
        else
            minP = pointDist;

         */
        if (Isx) {
            if (curr.left != null) {
                RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), curr.point.x(), rect.ymax());
                currDist = findNearP.distanceSquaredTo(curr.left.point);
                if (currDist < minP.dist)
                    minP = new PointDist(curr.left.point, currDist);
                if (minP.dist > leftRect.distanceSquaredTo(findNearP)) {
                    p1 = findNearest(curr.left, leftRect, minP, false);
                    if (p1 != null)
                        if (p1.dist < minP.dist)
                            minP = p1;
                }
            }
            if (curr.right != null) {
                RectHV rightRect = new RectHV(curr.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                currDist = findNearP.distanceSquaredTo(curr.right.point);
                if (currDist < minP.dist)
                    minP = new PointDist(curr.right.point, currDist);
                if (minP.dist > rightRect.distanceSquaredTo(findNearP)) {
                    p2 = findNearest(curr.right, rightRect, minP, false);
                    if (p2 != null)
                        if (p2.dist < minP.dist)
                            minP = p2;
                }
            }
        }
        else {
            if (curr.left != null) {
                RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), curr.point.y());
                currDist = findNearP.distanceSquaredTo(curr.left.point);
                if (currDist < minP.dist)
                    minP = new PointDist(curr.left.point, currDist);
                if (minP.dist > leftRect.distanceSquaredTo(findNearP)) {
                    p1 = findNearest(curr.left, leftRect, minP, true);
                    if (p1 != null)
                        if (p1.dist < minP.dist)
                            minP = p1;
                }
            }
            if (curr.right != null) {
                RectHV rightRect = new RectHV(rect.xmin(), curr.point.y(), rect.xmax(), rect.ymax());
                currDist = findNearP.distanceSquaredTo(curr.right.point);
                if (currDist < minP.dist)
                    minP = new PointDist(curr.right.point, currDist);
                if (minP.dist > rightRect.distanceSquaredTo(findNearP)) {
                    p2 = findNearest(curr.right, rightRect, minP, true);
                    if (p2 != null)
                        if (p2.dist < minP.dist)
                            minP = p2;
                }
            }
        }
        return minP;
    }

    private class PointDist {
        Point2D point;
        double dist;
        public PointDist(Point2D point, double dist) {
            this.point = point;
            this.dist = dist;
        }
    }


    private Iterable<Point2D> allPoints() {
        //return point2DS;
        return new KdTreeIterable(root, n);
    }


    private class KdTreeIterable implements Iterable<Point2D> {
        Node[] nodeStack;
        int index;
        public KdTreeIterable(Node curr, int len) {
            nodeStack = new Node[len];
            index = 0;
            if (isEmpty())
                throw new IllegalArgumentException("Tree is null");
            preorder(curr);
        }

        public KdTreeIterable(Node[] arrNode, int len) {
            nodeStack = new Node[len];
            index = len;
            System.arraycopy(arrNode, 0, nodeStack, 0, len);
        }

        private void preorder(Node curr) {
            if (curr == null)
                return;
            preorder(curr.left);
            nodeStack[index++] = curr;
            //System.out.println("Index" + index);
            //System.out.println("Point" +  nodeStack[index - 1].point);
            preorder(curr.right);
        }

        public Iterator<Point2D> iterator() {
            return new NodeIterator();
        }

        private class NodeIterator implements Iterator<Point2D> {
            int niterator = 0;
            public boolean hasNext() {
                return niterator<index;
            }

            public Point2D next() {
                if (hasNext())
                    return nodeStack[niterator++].point;
                else
                    throw new NoSuchElementException("Reached end of list");
            }

            public void remove() {
                /* Not supported */
            }
        }
    }

    public static void main(String[] args) {

    }
}
