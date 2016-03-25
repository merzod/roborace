package com.merzod.engne2d.utils;

import com.merzod.engine2d.objects.Line;
import com.merzod.engine2d.objects.Point;
import com.merzod.engine2d.utils.LineUtils;
import junit.framework.TestCase;

public class LineUtilsTest extends TestCase {
    public void testFindIntersection() throws Exception {
        // lines crossed in the middle
        Line line1 = new Line(0,5,10,5);
        Line line2 = new Line(0,0,10,10);
        Point p = LineUtils.findIntersection(line1, line2);
        assertNotNull(p);
        assertEquals(new Point(5,5), p);
        // lines are parallel
        line2 = new Line(0,0,10,0);
        p = LineUtils.findIntersection(line1, line2);
        assertNull(p);
        // lines touch each other at the end
        line2 = new Line(0,0,10,5);
        p = LineUtils.findIntersection(line1, line2);
        assertNotNull(p);
        assertEquals(new Point(10,5), p);
        // lines don't crossed
        line2 = new Line(0,0,5,3);
        p = LineUtils.findIntersection(line1, line2);
        assertNull(p);
    }

    public void testGetDistance() throws Exception {
        double len = LineUtils.getDistance(new Point(0, 0), new Point(10, 10));
        assertEquals(Math.sqrt(200), len);
    }

    public void testMoveLine() throws Exception {
        Line line = new Line(0,0,10,10);
        LineUtils.moveLine(line, new Point(10, 10));
        assertEquals(new Line(10,10,20,20), line);
    }

    public void testRotate() throws Exception {
        Line line = new Line(0,0,10,0);
        LineUtils.rotate(line, Math.toRadians(90));
        assertEquals(0, (int) line.getEnd().getX());
        assertEquals(10, (int)line.getEnd().getY());
    }

    public void testGetAngle() throws Exception {
        Line line = new Line(0,0,10,10);
        double angle = LineUtils.getAngle(line);
        assertEquals(45, (int)Math.toDegrees(angle));
    }

    public void testBuildLine() throws Exception {
        Line line = LineUtils.buildLine(new Point(0, 0), Math.sqrt(200), Math.toRadians(45));
        assertEquals(10, (int)line.getEnd().getX());
        assertEquals(10, (int)line.getEnd().getY());
    }

    public void testShiftPoint() throws Exception {
        Point p = new Point(0,0);
        LineUtils.shiftPoint(p, new Point(10, 10));
        assertEquals(new Point(10, 10), p);
    }
}
