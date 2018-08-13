
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *
 * @author devin
 */
public class KdTree {

    private static class Node {

        private final Point2D point;
        private final RectHV rectangle;
        private Node leftSubtree = null;
        private Node rightSubtree = null;

        public Node(Point2D point, RectHV rectangle) {
            this.point = point;
            this.rectangle = rectangle;
        }

        public Point2D getPoint() {
            return point;
        }

        public RectHV getRectHV() {
            return rectangle;
        }

        public void setLeftSubtree(Node n) {
            leftSubtree = n;
        }

        public Node getLeftSubtree() {
            return leftSubtree;
        }

        public void setRightSubtree(Node n) {
            rightSubtree = n;
        }

        public Node getRightSubtree() {
            return rightSubtree;
        }

        @Override
        public String toString() {
            return "point=" + point + ", rect=" + rectangle;
        }
    }

    private Node root;
    private int size;

    public KdTree() {
        // construct an empty set of points
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the set empty? 
        return root == null;
    }

    public int size() {
        // number of points in the set 
        return size;
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1));
            size++;
        } else {
            boolean inserted = insertNode(p, root, true); //recursive here
            if (inserted) {
                size++;
            }
        }
    }

    /**
     * This function is recursive.
     *
     * @param point
     * @param parent
     * @param vertical
     */
    private boolean insertNode(Point2D point, Node parent, boolean vertical) {

        if (point.equals(parent.getPoint())) {
            return false;
        }

        //compare x coordinates
        //(if the point to be inserted has a smaller x-coordinate 
        //than the point at the root, go left; otherwise go right);
        //compare y coordinates
        //(if the point to be inserted has a smaller y-coordinate 
        //than the point in the node, go left; otherwise go right);
        //vertical == true means the point is represented by a vertical line and x should be used for comparison
        boolean goLeft = comparePoints(point, parent.getPoint(), vertical);

        Node n = goLeft ? parent.getLeftSubtree() : parent.getRightSubtree();
        if (n == null) {

            RectHV parentRect = parent.getRectHV();

            //new node
            if (goLeft) {
                Node newNode;
                if (vertical) {
                    //just compared vertical, want horizontal limits
                    newNode = new Node(point,
                            new RectHV(parentRect.xmin(), parentRect.ymin(), parent.point.x(), parentRect.ymax()));
                } else {
                    newNode = new Node(point,
                            new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), parent.point.y()));
                }
                parent.setLeftSubtree(newNode);
                return true;
            } else {
                Node newNode;
                if (vertical) {
                    //just compared vertical, want horizontal limits
                    newNode = new Node(point,
                            new RectHV(parent.point.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax()));
                } else {
                    newNode = new Node(point,
                            new RectHV(parentRect.xmin(), parent.point.y(), parentRect.xmax(), parentRect.ymax()));
                }
                parent.setRightSubtree(newNode);
                return true;
            }
        } else {
            return insertNode(point, n, !vertical);
        }

    }

    /**
     * Helper method to compare points in the 2D tree given the node's current
     * alignment.
     *
     * If vertical then compare the y coordinates, or horizon then compare the y
     * coordinates.
     *
     * @param p1
     * @param p2
     * @param vertical
     * @return if true go left, if false go right
     */
    private boolean comparePoints(Point2D p1, Point2D p2, boolean vertical) {
        return vertical
                ? Double.compare(p1.x(), p2.x()) <= 0
                : Double.compare(p1.y(), p2.y()) <= 0;
    }

    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return false;
        } else {
            return findPoint(p, root, true);
        }
    }

    /**
     * Recursive function.
     *
     * @param p
     * @param n
     * @param vertical
     * @return
     */
    private boolean findPoint(Point2D p, Node n, boolean vertical) {
        if (p.equals(n.getPoint())) {
            return true;
        } else {
            //not equal, check subtrees
            //which subtree should we check?
            boolean goLeft = comparePoints(p, n.getPoint(), vertical);
            Node toCheck = goLeft ? n.getLeftSubtree() : n.getRightSubtree();
            if (toCheck == null) {
                return false;
            } else {
                return findPoint(p, toCheck, !vertical);
            }
        }
    }

    public void draw() {
        // draw all points to standard draw 
        drawNode(root, false);
        StdDraw.show();
    }

    /**
     * Recursive function to draw.
     *
     * @param n
     * @param horizontal
     */
    private void drawNode(Node n, boolean horizontal) {
        if (n == null) {
            return; //nothing left to do
        }
        //draw n
        if (horizontal) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.001);
            //y1 = y2
            StdDraw.line(n.rectangle.xmin(), n.point.y(), n.rectangle.xmax(), n.point.y());

        } else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.001);
            StdDraw.line(n.point.x(), n.rectangle.ymin(), n.point.x(), n.rectangle.ymax());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.getPoint().draw();

        if (n.getLeftSubtree() != null) {
            drawNode(n.getLeftSubtree(), !horizontal);
        }
        if (n.getRightSubtree() != null) {
            drawNode(n.getRightSubtree(), !horizontal);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> rangePoints = new ArrayList<>();
        if (root == null) {
            return rangePoints;
        }

        range(rect, rangePoints, root, true); //recursive call
        return rangePoints;
    }

    private void range(RectHV rect, ArrayList<Point2D> rangePoints, Node toCheck, boolean vertical) {

        if (toCheck == null) {
            return;
        }

        if (rect.contains(toCheck.getPoint())) {
            rangePoints.add(toCheck.getPoint());
        }

        //construct line to check if split by query rectangle
        RectHV rectToCheck = vertical
                ? new RectHV(toCheck.getPoint().x(), toCheck.getRectHV().ymin(),
                        toCheck.getPoint().x(), toCheck.getRectHV().ymax())
                : new RectHV(toCheck.getRectHV().xmin(), toCheck.getPoint().y(),
                        toCheck.getRectHV().xmax(), toCheck.getPoint().y());

        if (rect.intersects(rectToCheck)) {
            //we have to check both
            range(rect, rangePoints, toCheck.getLeftSubtree(), !vertical);
            range(rect, rangePoints, toCheck.getRightSubtree(), !vertical);

        } else {
            //not in the rectangle, which tree to check?

            //if vertical check left or right to x coordinates
            if (vertical) {
                if (Double.compare(rect.xmax(), toCheck.getPoint().x()) < 0) {
                    //go left
                    range(rect, rangePoints, toCheck.getLeftSubtree(), !vertical);
                } else {
                    //xmax is greater
                    //already check if intersects above so must be to right
                    range(rect, rangePoints, toCheck.getRightSubtree(), !vertical);
                }
            } else {
                //horizontal
                if (Double.compare(rect.ymax(), toCheck.getPoint().y()) < 0) {
                    //go left
                    range(rect, rangePoints, toCheck.getLeftSubtree(), !vertical);
                } else {
                    //xmax is greater
                    //already check if intersects above so must be to right
                    range(rect, rangePoints, toCheck.getRightSubtree(), !vertical);
                }
            }
        }
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (root == null) {
            return null;
        }

        Node currentNearest = null;

        currentNearest = nearest(p, root, currentNearest);

        return currentNearest.getPoint();
    }

    private Node nearest(Point2D p, Node parent, Node currentNearest) {

        if (parent == null) {
            return currentNearest; //can't go any further down the tree
        }

        //get the distance
        double distance = parent.getPoint().distanceSquaredTo(p);

        //should we keep checking this node?
        
        //check if we need to update for a new nearest
        if (currentNearest == null) {
            currentNearest = parent;
        }

        //todo, cache this in a class so we don't have to keep recalculating?
        double currentDistance = currentNearest.getPoint().distanceSquaredTo(p);
        
        if(Double.compare(distance, currentDistance) < 0) {
            currentNearest = parent;
            currentDistance = distance;
        }
        
        //should we keep exploring the subtrees?
        if(parent.getLeftSubtree() != null) {
            if(parent.getLeftSubtree().getRectHV().distanceSquaredTo(p) < currentDistance) {
                currentNearest = nearest(p, parent.getLeftSubtree(), currentNearest);
            }
        }

        if(parent.getRightSubtree() != null) {
            if(parent.getRightSubtree().getRectHV().distanceSquaredTo(p) < currentDistance) {
                currentNearest = nearest(p, parent.getRightSubtree(), currentNearest);
            }
        }
        
        return currentNearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        KdTree kd = new KdTree();

        assert (kd.size() == 0);
        assert (kd.isEmpty());
        assert(kd.nearest(new Point2D(1,1)) == null);

        kd.insert(new Point2D(0.7, 0.2));
        assert (kd.contains(new Point2D(0.7, 0.2)));
        assert(kd.nearest(new Point2D(1,1)).equals(new Point2D(0.7, 0.2)));

        assert (kd.size() == 1);
        assert (!kd.isEmpty());

        //dupe
        kd.insert(new Point2D(0.7, 0.2));
        assert (kd.contains(new Point2D(0.7, 0.2)));

        assert (kd.size() == 1);
        assert (!kd.isEmpty());

        kd.insert(new Point2D(0.5, 0.4));
        assert (kd.contains(new Point2D(0.5, 0.4)));
        assert(kd.nearest(new Point2D(0.4,0.4)).equals(new Point2D(0.5, 0.4)));

        assert (kd.size() == 2);
        assert (!kd.isEmpty());

        kd.insert(new Point2D(0.2, 0.3));
        assert (kd.contains(new Point2D(0.2, 0.3)));
        assert(kd.nearest(new Point2D(0.1,0.2)).equals(new Point2D(0.2, 0.3)));

        assert (kd.size() == 3);
        assert (!kd.isEmpty());

        kd.insert(new Point2D(0.4, 0.7));
        assert (kd.contains(new Point2D(0.4, 0.7)));

        assert (kd.size() == 4);
        assert (!kd.isEmpty());

        //dupe
        kd.insert(new Point2D(0.4, 0.7));
        assert (kd.contains(new Point2D(0.4, 0.7)));
        assert (kd.size() == 4);
        assert (!kd.isEmpty());

        kd.insert(new Point2D(0.9, 0.6));
        assert (kd.contains(new Point2D(0.9, 0.6)));

        assert (kd.size() == 5);
        assert (!kd.isEmpty());

        assert (!kd.contains(new Point2D(0, 0)));
        assert (!kd.contains(new Point2D(1, 1)));

        assert(kd.nearest(new Point2D(0.633, 0.78)).equals(new Point2D(0.4, 0.7)));
        
        kd.draw();
    }

}
