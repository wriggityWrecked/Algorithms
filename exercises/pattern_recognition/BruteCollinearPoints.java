
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 * @author devin
 */
public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments;

    private class BetterLineSegment implements Comparable<BetterLineSegment> {

        final private Point x;
        final private Point y;
        final private double slope;

        /**
         * LineSegment doesn't allow access to input Points. This does so we can
         * examine while comparing.
         *
         * @param x
         * @param y
         * @param slope
         */
        public BetterLineSegment(Point x, Point y, double slope) {
            this.x = x;
            this.y = y;
            this.slope = slope;
        }

        public Point getX() {
            return x;
        }

        public Point getY() {
            return y;
        }

        public double getSlope() {
            return slope;
        }

        @Override
        public int compareTo(BetterLineSegment bls) {
            int ret = Double.compare(getSlope(), bls.getSlope());
            if (ret == 0) {
                ret = y.compareTo(bls.getY());
            }
            if (ret == 0) {
                ret = x.compareTo(bls.getX());
            }
            return ret;
        }

        @Override
        public String toString() {
            return x + " -> " + y;
        }
    }

    public BruteCollinearPoints(Point[] points) {

        // The order of growth of the running time of your program should be n4 
        // in the worst case and it should use space 
        // proportional to n plus the number of line segments returned.
        // finds all line segments containing 4 points
        if (points == null) {
            throw new java.lang.IllegalArgumentException("input Point array cannot be null");
        }

        final Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, pointsCopy.length);
        final ArrayList<BetterLineSegment> betterSegmentList = new ArrayList<>();
        segments = new ArrayList<>();

        for (int i = 0; i < pointsCopy.length; i++) {

            //TODO get min and max of line
            if (pointsCopy[i] == null) {
                throw new java.lang.IllegalArgumentException("input Point cannot be null");
            }

            for (int j = 0; j < pointsCopy.length; j++) {

                if (pointsCopy[j] == null) {
                    throw new java.lang.IllegalArgumentException("input Point cannot be null");
                }

                if (i == j) {
                    continue;
                }

                if (pointsCopy[i].compareTo(pointsCopy[j]) == 0) {
                    throw new java.lang.IllegalArgumentException("input Point cannot be equal (duplicate)");
                }

                for (int k = 0; k < pointsCopy.length; k++) {

                    if (pointsCopy[k] == null) {
                        throw new java.lang.IllegalArgumentException("input Point cannot be null");
                    }

                    if (i == k || j == k) {
                        continue;
                    }

                    if (pointsCopy[j].compareTo(pointsCopy[k]) == 0) {
                        throw new java.lang.IllegalArgumentException("input Point cannot be equal (duplicate)");
                    }

                    for (int l = 0; l < pointsCopy.length; l++) {

                        if (pointsCopy[l] == null) {
                            throw new java.lang.IllegalArgumentException("input Point cannot be null");
                        }

                        if (i == l || j == l || k == l) {
                            continue;
                        }

                        int k_compare_l = pointsCopy[k].compareTo(pointsCopy[l]);

                        if (k_compare_l == 0) {
                            throw new java.lang.IllegalArgumentException("input Point cannot be equal (duplicate)");
                        }

                        double slope1 = pointsCopy[i].slopeTo(pointsCopy[j]);
                        double slope2 = pointsCopy[i].slopeTo(pointsCopy[k]);

                        if (Double.compare(slope1, slope2) == 0) {

                            double slope3 = pointsCopy[i].slopeTo(pointsCopy[l]);
                            if (Double.compare(slope1, slope3) == 0) {

                                // make sure we have the right endpoints
                                Point min = pointsCopy[l];
                                Point max = pointsCopy[l];

                                if (k_compare_l < 0) {
                                    min = pointsCopy[k];
                                }
                                if (k_compare_l > 0) {
                                    max = pointsCopy[k];
                                }

                                if (pointsCopy[j].compareTo(min) < 0) {
                                    min = pointsCopy[j];
                                }
                                if (pointsCopy[j].compareTo(max) > 0) {
                                    max = pointsCopy[j];
                                }

                                if (pointsCopy[i].compareTo(min) < 0) {
                                    min = pointsCopy[i];
                                }
                                if (pointsCopy[i].compareTo(max) > 0) {
                                    max = pointsCopy[i];
                                }

                                boolean seenLine = false;
                                BetterLineSegment proposedSegment = new BetterLineSegment(min, max, slope1);

                                // have we seen this slope before?
                                for (BetterLineSegment bls : betterSegmentList) {
                                    if (bls.compareTo(proposedSegment) == 0) {
                                        seenLine = true;
                                        break;
                                    }
                                }

                                // otherwise add as new
                                if (!seenLine) {
                                    betterSegmentList.add(proposedSegment);
                                    segments.add(new LineSegment(min, max));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return segments.size();
    }

    public LineSegment[] segments() {
        // the line segments
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
//        StdDraw.enableDoubleBuffering();
//        StdDraw.setXscale(0, 32768);
//        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();
        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        for (LineSegment ls : bcp.segments()) {
            System.out.println(ls);
        }
    }
}
