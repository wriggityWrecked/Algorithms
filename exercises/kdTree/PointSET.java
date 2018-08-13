import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/pstree.html
 *
 * @author devin
 */
public class PointSET {

    private final TreeSet<Point2D> set;
    public PointSET() {
        // construct an empty set of points
        set = new TreeSet<>(); //comparator?
    }

    public boolean isEmpty() {
        // is the set empty? 
        return set.isEmpty();
    }

    public int size() {
        // number of points in the set 
        return set.size();
    }

    public void insert(Point2D p) {
        // add the point to the set (if it is not already in the set)
        if(p == null) {
            throw new IllegalArgumentException();
        }
        
        set.add(p);
    }

    public boolean contains(Point2D p) {
        // does the set contain point p? 
        if(p == null) {
            throw new IllegalArgumentException();
        }
        
        return set.contains(p);
    }

    public void draw() {
        // draw all points to standard draw 
        StdDraw.setPenColor(edu.princeton.cs.algs4.StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for(Point2D p: set) {
            p.draw();
        }
        StdDraw.show();
    }


    public Iterable<Point2D> range(RectHV rect) {
        
        if(rect == null) {
            throw new IllegalArgumentException();
        }
        
        // all points that are inside the rectangle (or on the boundary)
        ArrayList<Point2D> allInside = new ArrayList<>();
        for(Point2D p: set) {
            if(rect.contains(p)) {
                allInside.add(p);
            }
        }
        return allInside;
    }


    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to point p; null if the set is empty
        if(p == null) {
            throw new IllegalArgumentException();
        }
        
        double distance = Double.NaN;
        Point2D min = null;
        
        for(Point2D pp: set) {
            double dist = p.distanceSquaredTo(pp);
            if(Double.isNaN(distance) || dist < distance) {
                min = pp;
                distance = dist;
            }
        }
        return min;
    }


    public static void main(String[] args) {
        PointSET ps = new PointSET();
        
        assert(ps.size() == 0);
        assert(ps.isEmpty());
        assert(ps.nearest(new Point2D(1,1)) == null);
        
        ps.insert(new Point2D(0.7, 0.2));
        assert(ps.contains(new Point2D(0.7, 0.2)));
        
        assert(ps.size() == 1);
        assert(!ps.isEmpty());        

        ps.insert(new Point2D(0.5, 0.4));
        assert(ps.contains(new Point2D(0.5, 0.4)));

        assert(ps.size() == 2);
        assert(!ps.isEmpty());   
        
        ps.insert(new Point2D(0.2, 0.3));
        assert(ps.contains(new Point2D(0.2, 0.3)));

        assert(ps.size() == 3);
        assert(!ps.isEmpty());   
        
        ps.insert(new Point2D(0.4, 0.7));
        assert(ps.contains(new Point2D(0.4, 0.7)));

        assert(ps.size() == 4);
        assert(!ps.isEmpty());   
        
        ps.insert(new Point2D(0.9, 0.6));
        assert(ps.contains(new Point2D(0.9, 0.6)));

        assert(ps.size() == 5);
        assert(!ps.isEmpty());   
        
        assert(!ps.contains(new Point2D(0,0)));
        assert(!ps.contains(new Point2D(1,1)));
        
        Point2D nearest = ps.nearest(new Point2D(0.21, 0.32));
        assert(nearest.equals(new Point2D(0.2,0.3)));
        
        ps.draw();
    }

}
