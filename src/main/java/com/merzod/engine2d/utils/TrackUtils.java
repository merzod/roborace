package com.merzod.engine2d.utils;

import com.merzod.engine2d.objects.Line;

import java.util.List;

public class TrackUtils {
    public static void buildOTrack(List<Line> lines) {
        lines.clear();
        lines.add(new Line(10, 10, 800, 10));
        lines.add(new Line(800, 10, 800, 500));
        lines.add(new Line(800, 500, 10, 500));
        lines.add(new Line(10, 500, 10, 10));
        lines.add(new Line(160, 245, 650, 245));
    }

    public static void buildLTrack(List<Line> lines) {
        lines.clear();
        lines.add(new Line(10, 10, 200, 10));
        lines.add(new Line(200, 10, 200, 200));
        lines.add(new Line(200, 200, 400, 200));
        lines.add(new Line(400, 200, 400, 400));
        lines.add(new Line(400, 400, 10, 400));
        lines.add(new Line(10, 400, 10, 10));

        lines.add(new Line(100, 100, 100, 300));
        lines.add(new Line(100, 300, 300, 300));
    }

    public static void buildUTrack(List<Line> lines) {
        lines.clear();
        lines.add(new Line(10, 10, 400, 10));
        lines.add(new Line(400, 10, 400, 400));
        lines.add(new Line(400, 400, 10, 400));
        lines.add(new Line(10, 400, 10, 10));

        lines.add(new Line(200, 10, 200, 200));
        lines.add(new Line(100, 100, 100, 300));
        lines.add(new Line(100, 300, 300, 300));
        lines.add(new Line(300, 300, 300, 100));
    }
}
