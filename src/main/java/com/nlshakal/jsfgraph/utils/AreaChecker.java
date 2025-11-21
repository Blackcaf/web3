package com.nlshakal.jsfgraph.utils;

public class AreaChecker {
    public static boolean isInArea(double x, double y, double r) {
        if (r == 0) {
            return false;
        }
        if (x <= 0 && y >= 0) {
            return x >= -r / 2 && y <= r && y <= 2 * x + r;
        }
        else if (x >= 0 && y >= 0) {
            return x <= r && y <= r;
        }
        else if (x >= 0 && y <= 0) {
            return (x * x + y * y) <= (r * r);
        }
        return false;
    }
}
