package com.merzod.engine2d.utils;

import com.merzod.engine2d.objects.Car;
import com.merzod.engine2d.objects.Line;
import com.merzod.engine2d.objects.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarUtils {

    private static final Logger log = LoggerFactory.getLogger(CarUtils.class);

    /**
     * Rotate care on specific angle
     * @param car car to rotate
     * @param angle angle in radians
     */
    public static void rotate(Car car, double angle) {
        log.debug("rotate {} to {}", car, Math.toDegrees(angle));
        car.setCarAngle(car.getCarAngle() + angle);
        for (Line sonar : car.getSonars()) {
            LineUtils.rotate(sonar, angle);
        }
    }

    /**
     * Find intersections between car and map. Information about found intersection for particular sonar passed to
     * collector. It's responsibility to store and provide it. The information provided as 3 attributes: sonar,
     * calculated distance and intersection point. If there is no intersection then point is null, distance is 0.
     * @param car car to check
     * @param map map lines
     * @param collector collector which will keep intersection info. The root cause of using collector interface,
     *                  is because different information about intersections is required sometimes, e.g. for UI only
     *                  collection of Points is interested, and for Driver only Sonar lines and distances for each.
     */
    public static void findCarIntersections(Car car, List<Line> map, IIntersectionsCollector collector) {
        log.debug("looking for car {} and map intersection", car);
        for (Line sonar : car.getSonars()) {
            // iterate over sonars, each sonar can intersect with many map lines, we need to find nearest map line
            double minDistance = 0;
            Point minPoint = null;
            for (Line line : map) {
                Point p = LineUtils.findIntersection(sonar, line);
                if(p != null) {
                    double distance = LineUtils.getDistance(sonar.getStart(), p);
                    if(minPoint == null || distance < minDistance) {
                        minDistance = distance;
                        minPoint = p;
                    }
                }
            }
            log.debug("min distance for sonar {} is {} point {}", sonar, minDistance, minPoint);
            collector.intersect(sonar, minPoint, minDistance);
        }
    }

    public static double findCarFrontIntersection(Car car, List<Line> map) {
        double angle = car.getCarAngle();
        Line sonar = LineUtils.buildLine(car.getPosition(), Car.MAX_SONAR, angle);
        double minDistance = 0;
        Point minPoint = null;
        for (Line line : map) {
            Point p = LineUtils.findIntersection(sonar, line);
            if(p != null) {
                double distance = LineUtils.getDistance(sonar.getStart(), p);
                if(minPoint == null || distance < minDistance) {
                    minDistance = distance;
                    minPoint = p;
                }
            }
        }
        log.debug("min distance for sonar {} is {} point {}", sonar, minDistance, minPoint);
        return minDistance;
    }

    /**
     * Move car to specific position, this will update all sonar lines
     * @param car car to move
     * @param position new position
     */
    public static void move(Car car, Point position) {
        log.debug("moving car {} to {}", car, position);
        car.setPosition(position);
        for (Line sonar : car.getSonars()) {
            LineUtils.moveLine(sonar, position);
        }
    }

    /**
     * Intersections collector, must keep information about sonars intersection and provide it when required
     * @param <T> Type of result which will be return on request
     */
    public interface IIntersectionsCollector<T> {
        /**
         * Called by calculation logic when intersection is found
         * @param sonar sonar line
         * @param point point of intersection
         * @param distance calculated distance
         */
        void intersect(Line sonar, Point point, double distance);

        /**
         * Get collected results if particular format
         * @return result
         */
        T getResult();
    }

}
