package com.britten.ui;

import com.britten.domain.Intersection;
import com.britten.domain.Road;
import com.britten.domain.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsciiRenderer {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 20;

    private static class Pos {
        int x, y;
        Pos(int x, int y) { this.x = x; this.y = y; }
    }

    // Feste Koordinaten für den Square-Graph
    // i1 (oben links), i2 (oben rechts)
    // i4 (unten links), i3 (unten rechts)
    private static final Map<Intersection, Pos> intersectionLayout = new HashMap<>();

    /**
     * Wird einmal beim Laden gesetzt.
     */
    public static void defineSquareLayout(
            Intersection i1,
            Intersection i2,
            Intersection i3,
            Intersection i4
    ) {
        intersectionLayout.put(i1, new Pos(5, 2));
        intersectionLayout.put(i2, new Pos(40, 2));
        intersectionLayout.put(i3, new Pos(40, 12));
        intersectionLayout.put(i4, new Pos(5, 12));
    }

    public static void render(List<Road> roads, List<Vehicle> vehicles) {
        char[][] grid = new char[HEIGHT][WIDTH];

        // Grid leeren
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                grid[y][x] = ' ';
            }
        }

        // Intersections zeichnen
        for (Road r : roads) {
            Pos pFrom = intersectionLayout.get(r.getFrom());
            Pos pTo = intersectionLayout.get(r.getTo());

            if (pFrom == null || pTo == null)
                continue;

            // Intersection-Knoten
            grid[pFrom.y][pFrom.x] = 'I';
            grid[pTo.y][pTo.x] = 'I';

            // Road als Linien zeichnen
            drawRoadLine(grid, pFrom, pTo);
        }

        // Fahrzeuge zeichnen
        for (Vehicle v : vehicles) {
            Road r = v.getCurrentRoad();
            Pos pFrom = intersectionLayout.get(r.getFrom());
            Pos pTo = intersectionLayout.get(r.getTo());

            if (pFrom == null || pTo == null)
                continue;

            int roadLen = r.getLength();
            double ratio = Math.min(1.0, Math.max(0.0, v.getPosition() / (double) roadLen));

            int x = (int) Math.round(pFrom.x + (pTo.x - pFrom.x) * ratio);
            int y = (int) Math.round(pFrom.y + (pTo.y - pFrom.y) * ratio);

            grid[y][x] = vehicleChar(v);
        }

        // Ausgabe
        System.out.println("\n===== ASCII MAP =====");
        for (int y = 0; y < HEIGHT; y++) {
            System.out.println(new String(grid[y]));
        }
        System.out.println("======================\n");
    }

    private static char vehicleChar(Vehicle v) {
        // Fahrzeuge als Zahlen oder Buchstaben
        if (v.getId() < 10) return (char) ('0' + v.getId());
        return (char) ('A' + (v.getId() % 26));
    }

    /**
     * Zeichnet die Road zwischen zwei Intersections.
     */
    private static void drawRoadLine(char[][] g, Pos a, Pos b) {

        int dx = Integer.signum(b.x - a.x);
        int dy = Integer.signum(b.y - a.y);

        int x = a.x;
        int y = a.y;

        while (x != b.x || y != b.y) {
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                if (g[y][x] == ' ')
                    g[y][x] = (dx != 0 ? '-' : '|');
            }
            x += dx;
            y += dy;
        }
    }
}
