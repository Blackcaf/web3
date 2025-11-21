package com.worthant.jsfgraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.worthant.jsfgraph.utils.AreaChecker;
import org.junit.jupiter.api.Test;

public class AreaCheckerTest {

    @Test
    public void testIsInAreaForUpperLeftRectangle() {
        // Inside the upper left rectangle (x: -R/2 to 0, y: 0 to R)
        assertTrue(AreaChecker.isInArea(-2.5, 5, 10));
        assertTrue(AreaChecker.isInArea(-5, 10, 10));
        assertTrue(AreaChecker.isInArea(-1, 7, 10));
        assertTrue(AreaChecker.isInArea(0, 0, 10));
        assertTrue(AreaChecker.isInArea(-5, 0, 10));
        assertTrue(AreaChecker.isInArea(-5, 5, 10));

        // Outside the upper left rectangle
        assertFalse(AreaChecker.isInArea(-5.1, 5, 10));
        assertFalse(AreaChecker.isInArea(-2, 10.1, 10));
    }

    @Test
    public void testIsInAreaForUpperRightRectangle() {
        // Inside the upper right rectangle (x: 0 to R/2, y: 0 to R/2)
        assertTrue(AreaChecker.isInArea(2.5, 2.5, 10));
        assertTrue(AreaChecker.isInArea(5, 5, 10));
        assertTrue(AreaChecker.isInArea(1, 3, 10));
        assertTrue(AreaChecker.isInArea(0, 0, 10));

        // Outside the upper right rectangle
        assertFalse(AreaChecker.isInArea(5.1, 2, 10));
        assertFalse(AreaChecker.isInArea(2, 5.1, 10));
        assertFalse(AreaChecker.isInArea(6, 0, 10));
    }

    @Test
    public void testIsInAreaForQuarterCircle() {
        // Inside the quarter circle (lower left quadrant, radius R)
        assertTrue(AreaChecker.isInArea(-7, -7, 10));
        assertTrue(AreaChecker.isInArea(-5, -5, 10));
        assertTrue(AreaChecker.isInArea(0, 0, 10));
        assertTrue(AreaChecker.isInArea(-3, -3, 10));

        // Outside the quarter circle
        assertFalse(AreaChecker.isInArea(-8, -8, 10));
        assertFalse(AreaChecker.isInArea(-10.1, 0, 10));
        assertFalse(AreaChecker.isInArea(0, -10.1, 10));
    }

    @Test
    public void testIsInAreaOutsideAllRegions() {
        // Outside all regions - bottom right quadrant
        assertFalse(AreaChecker.isInArea(5, -5, 10));
        assertFalse(AreaChecker.isInArea(10, -1, 10));

        // Outside all regions - beyond boundaries
        assertFalse(AreaChecker.isInArea(-456, 0, 10));
        assertFalse(AreaChecker.isInArea(4564, 0, 43));
        assertFalse(AreaChecker.isInArea(0, 354634563, 43));
        assertFalse(AreaChecker.isInArea(10, 10, 10));
    }

    @Test
    public void testForZeroCoords() {
        assertTrue(AreaChecker.isInArea(0, 0, 10),
                "Точка в центре должна быть в области");
        assertFalse(
                AreaChecker.isInArea(0, 0, 0),
                "При нулевом радиусе точка не должна быть внутри области, даже в центре");
    }

    @Test
    public void testBoundaryConditions() {
        // Граничные условия для четверти окружности
        assertTrue(AreaChecker.isInArea(0, -10, 10),
                "Точка на границе четверти окружности должна быть внутри.");
        assertTrue(AreaChecker.isInArea(-7.07, -7.07, 10),
                "Точка на границе четверти окружности должна быть внутри.");
        assertFalse(AreaChecker.isInArea(-10.1, -0.1, 10),
                "Точка вне границы четверти окружности должна быть снаружи.");

        // Граничные условия для верхнего левого прямоугольника
        assertTrue(AreaChecker.isInArea(-5, 10, 10),
                "Точка на границе прямоугольника должна быть внутри.");
        assertTrue(AreaChecker.isInArea(-5, 0, 10),
                "Точка на границе прямоугольника должна быть внутри.");
        assertFalse(AreaChecker.isInArea(-5.1, 5, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");
        assertFalse(AreaChecker.isInArea(-2, 10.1, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");

        // Граничные условия для верхнего правого прямоугольника
        assertTrue(AreaChecker.isInArea(5, 5, 10),
                "Точка на границе прямоугольника должна быть внутри.");
        assertFalse(AreaChecker.isInArea(5.1, 2, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");
        assertFalse(AreaChecker.isInArea(2, 5.1, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");
    }
}
