package org.example.util;

public final class AreaCheckUtil {

    private AreaCheckUtil() {}

    public static boolean checkPoint(Integer x, Double y, Double r) {
        if (x == null || y == null || r == null || r <= 0) {
            return false;
        }

        return checkRectangle(x, y, r) ||
                checkTriangle(x, y, r) ||
                checkCircle(x, y, r);
    }

    private static boolean checkRectangle(int x, double y, double r) {
        return x >= 0 && x <= r && y >= 0 && y <= r;
    }

    private static boolean checkTriangle(int x, double y, double r) {
        return x <= 0 && x >= -r / 2 && y >= 0 && y <= 2 * x + r;
    }

    private static boolean checkCircle(int x, double y, double r) {
        return x >= 0 && y <= 0 && (x * x + y * y) <= (r * r);
    }
}