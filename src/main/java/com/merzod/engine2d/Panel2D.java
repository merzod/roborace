package com.merzod.engine2d;

import com.merzod.engine2d.objects.*;
import com.merzod.engine2d.objects.Point;
import com.merzod.engine2d.utils.CarUtils;
import com.merzod.engine2d.utils.LineUtils;
import com.merzod.engine2d.utils.TrackUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class Panel2D extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(Panel2D.class);

    private Graphics2D g2;
    private Car car;
    private final List<Line> lines;
    private final Driver pilot;

    public Panel2D() {
        lines = new ArrayList<>();
        TrackUtils.buildUTrack(lines);
//        TrackUtils.buildOTrack(lines);
//        TrackUtils.buildLTrack(lines);

        car = new Car(new Point(150, 150), -45, 0, 45);
        pilot = new Driver(car);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D)g;

        // TODO: move to timer
        pilot.drive(lines);

        // find intersections and paint all the stuff
        CarUtils.IIntersectionsCollector<List<Point>> collector = new CarUtils.IIntersectionsCollector<List<Point>>() {
            private List<Point> points = new ArrayList<>();
            @Override
            public void intersect(Line sonar, Point point, double distance) {
                if(point != null) {
                    points.add(point);
                }
            }
            @Override
            public List<Point> getResult() {
                return points;
            }
        };
        CarUtils.findCarIntersections(car, lines, collector);

        // paint all the stuff
        for (Line line : lines) {
            paint(line, Color.BLACK);
        }
        paint(car, Color.BLUE);
        for (Point p : collector.getResult()) {
            paint(p, Color.RED);
        }
    }

    private void externalControl(double angle) {
        CarUtils.rotate(car, angle);
    }

    private void cycle() {
        // rotate to max angle if needed
        double angle = car.getWheelsAngle();
        log.debug("rotate to {}", Math.toDegrees(angle));
        if(angle != 0) {
            CarUtils.rotate(car, car.getWheelsAngle());
        }

        double frontDistance = CarUtils.findCarFrontIntersection(car, lines);
        if(frontDistance > Car.MAX_SPEED) {
            // move forward
            Point p = LineUtils.toDecart(Car.MAX_SPEED, car.getCarAngle());
            log.debug("point to angel {} is {}", Math.toDegrees(car.getCarAngle()), p);
            LineUtils.shiftPoint(p, car.getPosition());
            log.debug("move from {} to {}", car.getPosition(), p);
            CarUtils.move(car, p);
        } else {
            CarUtils.rotate(car, Math.toRadians(90));
        }
    }

    public void keyPressed(int code) {
        switch (code) {
            case 37: // left
                externalControl(Math.toRadians(-5));break;
            case 39: // right
                externalControl(Math.toRadians(5));break;
            case 32: // space
                cycle();break;
        }
        this.repaint();
    }

    public void paint(Line line, Color color) {
        Color old = g2.getColor();
        g2.setColor(color);
        g2.draw(new Line2D.Double(
                line.getStart().getX(), line.getStart().getY(),
                line.getEnd().getX(), line.getEnd().getY()));
        g2.setColor(old);
    }

    public void paintThick(Line line, Color color) {
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));
        g2.draw(new Line2D.Double(
                line.getStart().getX(), line.getStart().getY(),
                line.getEnd().getX(), line.getEnd().getY()));
        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    public void paint(Point point, Color color) {
        Color old = g2.getColor();
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(point.getX() - 5, point.getY() - 5, 10.0, 10.0));
        g2.setColor(old);
    }

    public void paint(Car car, Color color) {
        // paint wheels
        double angle = car.getWheelsAngle();
        angle += car.getCarAngle();
        Point start = car.getPosition();
        Point end = LineUtils.toDecart(Car.MAX_SONAR, angle);
        LineUtils.shiftPoint(end, start);
        paintThick(new Line(start, end), Color.GREEN);
        // paint position
        paint(car.getPosition(), color);
        // paint sonars
        for (Line sonar : car.getSonars()) {
            paint(sonar, color);
        }
    }
}
