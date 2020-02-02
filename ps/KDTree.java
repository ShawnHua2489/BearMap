package bearmaps.utils.ps;

import java.util.List;

public class KDTree implements PointSet {

    KDTreeNode root;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = insert(root, p, HORIZONTAL);
        }
    }

    /*
    You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion!
     */

    private KDTreeNode insert(KDTreeNode node, Point p, boolean orientation) {
        if (node == null) {
            return new KDTreeNode(p, orientation);
        }

        if (node.point.equals(p)) {
            return node;
        }

        int comparison = comparePoints(p, node.point, orientation);
        if (comparison < 0) {
            node.left = insert(node.left, p, !orientation);
        } else if (comparison >= 0) {
            node.right = insert(node.right, p, !orientation);
        }
        return node;
    }

    private int comparePoints(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(a.getX(), b.getX());
        }
        return Double.compare(a.getY(), b.getY());
    }

    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS. */
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        return nearestHelper(root, goal, root.point, HORIZONTAL);
    }

    private Point nearestHelper(KDTreeNode n, Point goal, Point best, boolean orientation) {
        KDTreeNode goodSide;
        KDTreeNode badSide;
        if (n == null) {
            return best;
        }

        if (Point.distance(n.point, goal) < Point.distance(best, goal)) {
            best = n.point;
        }

        int comparison = comparePoints(goal, n.point, orientation);
        if (comparison < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }

        best = nearestHelper(goodSide, goal, best, !orientation);
        best = nearestHelper(badSide, goal, best, !orientation);

        return best;
    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left; // also refers to down child
        private KDTreeNode right; // also refers to right child

        // If you want to add any more instance variables, put them here!
        private boolean orientation;

        KDTreeNode(Point p, boolean o) {
            this.point = p;
            this.orientation = o;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right, boolean o) {
            this.point = p;
            this.left = left;
            this.right = right;
            this.orientation = o;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        // If you want to add any more methods, put them here!
        boolean orientation() { return orientation; }

    }

    public static void main(String[] args) {
        Point a = new Point(2, 3);
        Point b = new Point(4, 2);
        Point c = new Point(4, 5);
        Point d = new Point(3, 3);
        Point e = new Point(1, 5);
        Point f = new Point(4, 4);
        KDTree kd = new KDTree(List.of(a, b, c, d, e, f));
    }
}
