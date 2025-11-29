package org.example.config;

import java.math.BigDecimal;
import java.util.Set;

public final class Config {

    private Config() {}

    private static final Set<Integer> ALLOWED_X = Set.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
    private static final BigDecimal MIN_Y = BigDecimal.valueOf(-5);
    private static final BigDecimal MAX_Y = BigDecimal.valueOf(5);
    private static final BigDecimal MIN_R = BigDecimal.valueOf(0.1);
    private static final BigDecimal MAX_R = BigDecimal.valueOf(3.0);

    public static Set<Integer> getAllowedX() { return ALLOWED_X; }
    public static BigDecimal getMinY() { return MIN_Y; }
    public static BigDecimal getMaxY() { return MAX_Y; }
    public static BigDecimal getMinR() { return MIN_R; }
    public static BigDecimal getMaxR() { return MAX_R; }
}