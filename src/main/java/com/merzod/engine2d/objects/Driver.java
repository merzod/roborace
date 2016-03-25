package com.merzod.engine2d.objects;

import com.merzod.engine2d.utils.CarUtils;
import com.merzod.engine2d.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Driver {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);
    private Car car;
    private double shift = 10;

    public Driver(Car car) {
        this.car = car;
    }

    public void drive(List<Line> map) {
        CarUtils.IIntersectionsCollector<Map<Line, Double>>  collector = new CarUtils.IIntersectionsCollector<Map<Line, Double>>() {
            private Map<Line, Double> intersections = new HashMap<>();
            @Override
            public void intersect(Line sonar, Point point, double distance) {
                if(point != null) {
                    intersections.put(sonar, distance);
                }
            }

            @Override
            public Map<Line, Double> getResult() {
                return intersections;
            }
        };
        CarUtils.findCarIntersections(car, map, collector);
        Map<Line, Double> inter = collector.getResult();
        // iterate over intersection map and find max sonar distance (sonar start point - intersection point)
        double maxDistance = 0;
        Line maxSonar = null;
        for (Map.Entry<Line, Double> entry : inter.entrySet()) {
            Double distance = entry.getValue();
            if(maxSonar == null || maxDistance < distance) {
                maxDistance = distance;
                maxSonar = entry.getKey();
            }
        }
        double maxAngle = LineUtils.getAngle(maxSonar);
        log.debug("max sonar found angle {}, car angle {}", Math.toDegrees(maxAngle), Math.toDegrees(car.getCarAngle()));
        maxAngle -= car.getCarAngle();// + Math.toRadians(shift);
        //shift *= -1;
        double degAngle = Math.toDegrees(maxAngle);
        log.info("driver turn to {}", degAngle);
        if(-1<degAngle && degAngle<1) {
            car.setWheelsAngle(Math.toRadians(0));
        } else if(degAngle<-5 || 5<degAngle) {
            if(degAngle > 0){
                car.setWheelsAngle(Math.toRadians(40));
            } else {
                car.setWheelsAngle(Math.toRadians(-40));
            }
            //car.setWheelsAngle(maxAngle);
        }
    }
}
