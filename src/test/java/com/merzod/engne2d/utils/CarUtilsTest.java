package com.merzod.engne2d.utils;

import com.merzod.engine2d.objects.Car;
import com.merzod.engine2d.objects.Line;
import com.merzod.engine2d.objects.Point;
import com.merzod.engine2d.utils.CarUtils;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CarUtilsTest extends TestCase {

    public void testRotate() throws Exception {
        Car car = new Car(new Point(0,0), -45, 0, 45);
        CarUtils.rotate(car, Math.toRadians(90));

        assertEquals(90, (int)Math.toDegrees(car.getCarAngle()));
    }

    public void testMove() throws Exception {
        Car car = new Car(new Point(0,0), -45, 0, 45);
        Point p = new Point(10, 10);
        CarUtils.move(car, p);

        assertEquals(p, car.getPosition());
        for (Line sonar : car.getSonars()) {
            assertEquals(p, sonar.getStart());
        }
    }

    public void testFindCarIntersections() throws Exception {
        Car car = new Car(new Point(10,10), -45, 0, 45);
        List<Line> map = new ArrayList<>();
        map.add(new Line(20, 0, 20, 20));
        CarUtils.IIntersectionsCollector<List<Point>> collector = new CarUtils.IIntersectionsCollector<List<Point>>() {
            List<Point> intersections = new ArrayList<>();
            @Override
            public void intersect(Line sonar, Point point, double distance) {
                intersections.add(point);
            }

            @Override
            public List<Point> getResult() {
                return intersections;
            }
        };
        CarUtils.findCarIntersections(car, map, collector);

        int i = 0;
        for (Point interPoint : collector.getResult()) {
            assertEquals(20, (int)interPoint.getX());
            assertEquals(i*10, (int)interPoint.getY());
            i++;
        }
    }
}
