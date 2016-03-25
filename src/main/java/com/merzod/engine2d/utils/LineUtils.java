package com.merzod.engine2d.utils;

import com.merzod.engine2d.objects.Line;
import com.merzod.engine2d.objects.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LineUtils {

    private static final Logger log = LoggerFactory.getLogger(LineUtils.class);

    /**
     * Find intersection between 2 lines
     * @param line1 line one
     * @param line2 line two
     * @return intersection point if there is any, null otherwise
     */
    public static Point findIntersection(Line line1, Line line2) {
        log.debug("looking for intersection between {} and {}", line1, line2);
        double x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = line1.getStart().getX();
        x2 = line1.getEnd().getX();
        x3 = line2.getStart().getX();
        x4 = line2.getEnd().getX();
        y1 = line1.getStart().getY();
        y2 = line1.getEnd().getY();
        y3 = line2.getStart().getY();
        y4 = line2.getEnd().getY();

        double Ua=((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/((y4-y3)*(x2-x1)-(x4-x3)*(y2-y1));
        double x=x1+Ua*(x2-x1);
        double y=y1+Ua*(y2-y1);
        log.debug("found [{}:{}]", x, y);

        if((isBetween(x, x1, x2) && isBetween(y, y1, y2)) &&
                (isBetween(x, x3, x4) && isBetween(y, y3, y4))) {
            log.debug("intersection found");
            return new Point(x, y);
        }
        log.debug("no intersection found");
        return null;
    }

    /**
     * Checks if x is between x1 and x2 with some approximation, due to not accurate Pi calculations
     * shift up to 1 unit in any direction possible. It doesn't matter if x1 less then x2 or otherwise
     * @param x coordinate to check
     * @param x1 limit one
     * @param x2 limit two
     * @return true if x is between x1 and x2, false otherwise
     */
    private static boolean isBetween(double x, double x1, double x2) {
        int _x = (int)x;
        int _x1 = (int)x1;
        int _x2 = (int)x2;

        return (_x1<=_x && _x<=_x2) || (_x2<=_x && _x<=_x1)||
                (_x1<=_x+1 && _x+1<=_x2) || (_x2<=_x+1 && _x+1<=_x1) ||
                (_x1<=_x-1 && _x-1<=_x2) || (_x2<=_x-1 && _x-1<=_x1);
    }

    /**
     * Calculates length of the line, calls getDistance()
     * @param line line to check
     * @return line length
     */
    public static double getLineLength(Line line) {
        return getDistance(line.getStart(), line.getEnd());
    }

    /**
     * Calculates distance between 2 points
     * @param p1 point one
     * @param p2 point two
     * @return distance between 2 points
     */
    public static double getDistance(Point p1, Point p2) {
        log.debug("looking for distance between {} and {}", p1, p2);
        double x1, x2, y1, y2;
        x1 = p1.getX();
        x2 = p2.getX();
        y1 = p1.getY();
        y2 = p2.getY();

        double dx, dy;
        dx = Math.abs(x2 - x1);
        dy = Math.abs(y2 - y1);
        double distance = Math.sqrt(dx * dx + dy * dy);
        log.debug("result {}", distance);
        return distance;
    }

    /**
     * Shift line to the point. Line's start point will be replaced with incoming point, end point will be calculated
     * @param line line to move
     * @param point new start point
     */
    public static void moveLine(Line line, Point point) {
        log.debug("moving line {} to {}", line, point);
        double dx, dy;
        dx = line.getEnd().getX() - line.getStart().getX();
        dy = line.getEnd().getY() - line.getStart().getY();

        line.setStart(point);
        line.setEnd(new Point(point.getX() + dx, point.getY() + dy));
        log.debug("result {}", line);
    }

    /**
     * Rotate line to particular degree, based on start point
     * @param line line to rotate
     * @param angle angle to rotate in radians
     */
    public static void rotate(Line line, double angle) {
        log.debug("rotate line {} to angle {}", line, Math.toDegrees(angle));
        double newAngle = getAngle(line);
        newAngle += angle;
        log.debug("new line angle {}", Math.toDegrees(newAngle));
        double p = LineUtils.getLineLength(line);
        line.setEnd(buildLine(line.getStart(), p, newAngle).getEnd());
        log.debug("result {}", line);
    }

    /**
     * Returns angle of the line in radians
     * @param line line to analyse
     * @return angle in radians
     */
    public static double getAngle(Line line) {
        log.debug("looking for angle of {}", line);
        double angle = Math.atan((line.getEnd().getY()-line.getStart().getY())/
                (line.getEnd().getX()-line.getStart().getX()));
        // if angle is NaN
        if(Double.isNaN(angle))
            angle = (135*3.1415927)/180;
        // If angle in 2,3 q
        if(line.getEnd().getX() < line.getStart().getX())
            angle = angle-3.1415927;
        log.debug("result {}", Math.toDegrees(angle));
        return angle;
    }

    /**
     * Build line by polar coordinates
     * @param p start point
     * @param len line length
     * @param angle angle in radians
     * @return line build by input polar coordinates
     */
    public static Line buildLine(Point p, double len, double angle) {
        log.debug("build line from {} with length {} and angle {}", p, len, Math.toDegrees(angle));
        Point end = toDecart(len, angle);
        shiftPoint(end, p);
        Line line = new Line(p, end);
        log.debug("result {}", line);
        return line;
    }

    /**
     * Get Decart coordinates by polar one
     * @param len length
     * @param angle angle in radians
     * @return calculated point
     */
    public static Point toDecart(double len, double angle) {
        double x = len * Math.cos(angle);
        double y = len * Math.sin(angle);
        return new Point(x, y);
    }

    /**
     * Shift point source to delta, basically add sum their coordinates
     * @param source point to shift
     * @param delta point which coordinates will be added to source
     */
    public static void shiftPoint(Point source, Point delta) {
        source.setX(source.getX() + delta.getX());
        source.setY(source.getY() + delta.getY());
    }
}
