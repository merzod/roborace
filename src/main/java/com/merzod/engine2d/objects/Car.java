package com.merzod.engine2d.objects;

import com.merzod.engine2d.utils.CarUtils;
import com.merzod.engine2d.utils.LineUtils;

import java.util.ArrayList;
import java.util.List;

public class Car {
    public static final int MAX_SONAR = 1000;
    public static final int MAX_SPEED = 5;

    private Point position;
    private List<Line> sonars;
    // all angles are in radians
    private double carAngle = 0; // global car angle
    private double wheelsAngle = 0; // relative wheel angle (based on carAngle)

    public Car(Point position, double... angles) {
        sonars = new ArrayList<>();

        for (double angle : angles) {
            addSonar(angle);
        }

        CarUtils.move(this, position);
    }

    private void addSonar(double angle) {
        sonars.add(LineUtils.buildLine(new Point(0, 0), MAX_SONAR, Math.toRadians(angle)));
    }

    public Point getPosition() {
        return position;
    }

    public List<Line> getSonars() {
        return sonars;
    }

    public double getWheelsAngle() {
        return wheelsAngle;
    }

    public void setWheelsAngle(double wheelsAngle) {
        this.wheelsAngle = wheelsAngle;
    }

    public double getCarAngle() {
        return carAngle;
    }

    public void setCarAngle(double carAngle) {
        this.carAngle = carAngle;
    }

    @Override
    public String toString() {
        return "Car "+position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
