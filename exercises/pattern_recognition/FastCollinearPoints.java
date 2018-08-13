
import java.util.ArrayList;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Arrays;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *
 * @author devin
 */
public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments;

    private static final BetterPointComparator POINT_COMPARATOR = new BetterPointComparator();

    private static final class BetterPointComparator implements Comparator<BetterPoint> {

        @Override
        public int compare(BetterPoint o1, BetterPoint o2) {

            // compare the slopes
            double slope1 = o1.getSlope();
            double slope2 = o2.getSlope();

            int ret = Double.compare(slope1, slope2);
            if (ret == 0) {
                ret = o1.getX().compareTo(o2.getX());
            }
            return ret;
        }
    };

    private class BetterPoint {

        private final Point x;
        private final double slope;

        /**
         *
         * @param x
         * @param y
         * @param slope
         */
        public BetterPoint(Point x, double slope) {
            this.x = x;
            this.slope = slope;
        }

        public Point getX() {
            return x;
        }

        public double getSlope() {
            return slope;
        }

        @Override
        public String toString() {
            return getSlope() + ", " + x;
        }
    }

    private static final BetterLineComparator LINE_COMPARATOR = new BetterLineComparator();

    private static final class BetterLineComparator implements Comparator<BetterLine> {

        @Override
        public int compare(BetterLine o1, BetterLine o2) {

            // compare the slopes
            double slope1 = o1.getSlope();
            double slope2 = o2.getSlope();

            int ret = Double.compare(slope1, slope2);
            if (ret == 0) {
                ret = o1.getX().compareTo(o2.getX());
            }
            if (ret == 0) {
                ret = o1.getY().compareTo(o2.getY());
            }
            return ret;
        }
    };

    private class BetterLine implements Comparable<BetterLine> {

        private final Point x;
        private final Point y;
        private final double slope;

        /**
         *
         * @param x
         * @param y
         * @param slope
         */
        public BetterLine(Point x, Point y, double slope) {
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
        public int compareTo(BetterLine o) {

            // compare the slopes
            double slope1 = this.getSlope();
            double slope2 = o.getSlope();

            int ret = Double.compare(slope1, slope2);
            if (ret == 0) {
                ret = this.getY().compareTo(o.getY());
            }
            if (ret == 0) {
                ret = this.getX().compareTo(o.getX());
            }
            return ret;
        }

        @Override
        public String toString() {
            return getSlope() + ", " + x + ", " + y +"\n";
        }

    }

    public FastCollinearPoints(Point[] points) {

        // Performance requirement. The order of growth of the running time of 
        // your program should be n2 log n in the worst case and it should use 
        // space proportional to n plus the number of line segments returned. 
        // FastCollinearPoints should work properly even if the input has 5 
        // or more collinear points.
        // finds all line segments containing 4 or more points
        if (points == null) {
            throw new java.lang.IllegalArgumentException("input Point array cannot be null");
        }

        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, pointsCopy.length);
        ArrayList<BetterLine> lines = new ArrayList<>();

        segments = new ArrayList<>();

        for (int i = 0; i < pointsCopy.length; i++) {


            // n-1 compares
            if (pointsCopy[i] == null) {
                throw new java.lang.IllegalArgumentException("input Point cannot be null");
            }

            for (int j = 1; j < pointsCopy.length; j++) {

                if (pointsCopy[j] == null) {
                    throw new java.lang.IllegalArgumentException("input Point cannot be null");
                }

                if (i == j) {
                    // don't self compare
                    continue;
                }

                if (pointsCopy[i].compareTo(pointsCopy[j]) == 0) {
                    //equal
                    throw new java.lang.IllegalArgumentException("input Point duplicate found");
                }

                //add to local segments
                //take min point as endpoint
                Point min = pointsCopy[i];
                Point max = pointsCopy[j];                
                if (pointsCopy[j].compareTo(pointsCopy[i]) < 0){
                    min = pointsCopy[j];
                    max = pointsCopy[i];
                }
                
                BetterLine bp = new BetterLine(min, max, pointsCopy[i].slopeTo(pointsCopy[j]));
                lines.add(bp);
            }
        }

        lines.sort(LINE_COMPARATOR);
        System.out.println(Arrays.toString(lines.toArray()));
        //look for collinear lines
        int equalSlopes = 1; //one to include the current slope
        Point firstEndPoint;
        BetterLine lastLineFound = null;
        firstEndPoint = lines.get(0).getX();
        
        for (int j = 1; j < lines.size(); j++) {

            BetterLine currentLine = lines.get(j);
            BetterLine previousLine = lines.get(j - 1);
            
            if(lastLineFound != null) {
                //min endpoint is greater than, but still a segment given last endpoint
                if(Double.compare(currentLine.getSlope(), lastLineFound.getSlope()) == 0 
                        && currentLine.getX().compareTo(lastLineFound.getX()) > 0 && currentLine.getY().compareTo(lastLineFound.getY()) <= 0) {
                    continue;
                }
            }
            
            //System.out.println("inspecting " + previousPoint + ", " + nextPoint);
            //if two points have the same slope and same endpoint (min)
            if (Double.compare(currentLine.getSlope(), previousLine.getSlope()) == 0) {

                //equal slope
                if(firstEndPoint.compareTo(currentLine.getX()) == 0) {
                    ///and equal first endpoint (min)
                    equalSlopes++;
                } else {
                    //first end point no longer equal, create a line if need be
                    if (equalSlopes >= 3) {
                        Point endpoint = previousLine.getY();
                        segments.add(new LineSegment(firstEndPoint, endpoint));
                        lastLineFound = new BetterLine(firstEndPoint, endpoint, previousLine.getSlope());
                    } else {
                        firstEndPoint = currentLine.getX();
                    }
                    //clear
                    equalSlopes = 1;
                }

            } else {
                //clear
                equalSlopes = 1;
                firstEndPoint = currentLine.getX();
            }

            //check if we are about to exit the loop
            if ((j == (lines.size() - 1)) && equalSlopes >= 3) {
                Point endpoint = currentLine.getY();
                segments.add(new LineSegment(firstEndPoint, endpoint));
            }
        }
    }

    private boolean seenPoint(ArrayList<BetterLine> sl, BetterPoint toCheck) {
        for (BetterLine bls : sl) {
            if (Double.compare(bls.getSlope(), toCheck.getSlope()) == 0) {
                //check if at least one of the endpoints is equal
                if (bls.getY().compareTo(toCheck.getX()) == 0 || bls.getX().compareTo(toCheck.getX()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean seenLine(ArrayList<BetterLine> sl, BetterLine toCheck) {
        for (BetterLine bls : sl) {
            if (Double.compare(bls.getSlope(), toCheck.getSlope()) == 0) {
                //check if at least one of the endpoints is equal
                if (bls.getY().compareTo(toCheck.getY()) == 0 || bls.getX().compareTo(toCheck.getX()) == 0) {
                    return true;
                }
            }
        }
        return false;
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
        edu.princeton.cs.algs4.In in = new edu.princeton.cs.algs4.In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setPenRadius(0.01);

        //edu.princeton.cs.algs4.StdDraw.show();
        FastCollinearPoints bcp = new FastCollinearPoints(points);
        System.out.println("NUMBER OF SEGMENTS: " + bcp.numberOfSegments());
        for (LineSegment ls : bcp.segments()) {
            System.out.println(ls);
            ls.draw();
        }
        StdDraw.show();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLUE);

        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    }
}
