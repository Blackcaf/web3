package com.nlshakal.jsfgraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nlshakal.jsfgraph.utils.AreaChecker;
import org.junit.jupiter.api.Test;

public class AreaCheckerTest {

    @Test
    public void testIsInAreaForUpperLeftTriangle() {
        // Треугольник во втором квадранте: вершины (0,0), (-R/2,0), (0,R)
        // Формула: y ≤ 2x + R
        assertTrue(AreaChecker.isInArea(0, 0, 10)); // Вершина
        assertTrue(AreaChecker.isInArea(-5, 0, 10)); // Вершина (-R/2, 0)
        assertTrue(AreaChecker.isInArea(0, 10, 10)); // Вершина (0, R)
        assertTrue(AreaChecker.isInArea(-2.5, 5, 10)); // Середина треугольника
        assertTrue(AreaChecker.isInArea(-1, 8, 10)); // Внутри треугольника

        // Вне треугольника
        assertFalse(AreaChecker.isInArea(-5, 5, 10)); // Вне треугольника (y > 2x + R)
        assertFalse(AreaChecker.isInArea(-5, 1, 10)); // Вне треугольника
        assertFalse(AreaChecker.isInArea(-6, 0, 10)); // x < -R/2
    }

    @Test
    public void testIsInAreaForUpperRightRectangle() {
        // Прямоугольник в первом квадранте: от 0 до R по X и от 0 до R по Y
        assertTrue(AreaChecker.isInArea(5, 5, 10)); // Внутри
        assertTrue(AreaChecker.isInArea(10, 10, 10)); // Вершина (R, R)
        assertTrue(AreaChecker.isInArea(0, 0, 10)); // Вершина (0, 0)
        assertTrue(AreaChecker.isInArea(3, 7, 10)); // Внутри

        // Вне прямоугольника
        assertFalse(AreaChecker.isInArea(10.1, 5, 10)); // x > R
        assertFalse(AreaChecker.isInArea(5, 10.1, 10)); // y > R
        assertFalse(AreaChecker.isInArea(11, 11, 10)); // Вне области
    }

    @Test
    public void testIsInAreaForQuarterCircle() {
        // Четверть круга в четвертом квадранте (x≥0, y≤0): радиус R
        assertTrue(AreaChecker.isInArea(7, -7, 10)); // Внутри круга
        assertTrue(AreaChecker.isInArea(0, -10, 10)); // На границе
        assertTrue(AreaChecker.isInArea(10, 0, 10)); // На границе
        assertTrue(AreaChecker.isInArea(0, 0, 10)); // Центр
        assertTrue(AreaChecker.isInArea(5, -5, 10)); // Внутри

        // Вне круга
        assertFalse(AreaChecker.isInArea(8, -8, 10)); // За пределами радиуса
        assertFalse(AreaChecker.isInArea(10.1, 0, 10)); // x > R
        assertFalse(AreaChecker.isInArea(0, -10.1, 10)); // y < -R
    }

    @Test
    public void testIsInAreaOutsideAllRegions() {
        // Вне всех областей - третий квадрант (x<0, y<0)
        assertFalse(AreaChecker.isInArea(-5, -5, 10));
        assertFalse(AreaChecker.isInArea(-1, -1, 10));

        // Далеко за пределами
        assertFalse(AreaChecker.isInArea(-456, 0, 10));
        assertFalse(AreaChecker.isInArea(4564, 0, 43));
        assertFalse(AreaChecker.isInArea(0, 354634563, 43));
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
        // Граничные условия для четверти круга (четвертый квадрант)
        assertTrue(AreaChecker.isInArea(0, -10, 10),
                "Точка на границе четверти окружности должна быть внутри.");
        assertTrue(AreaChecker.isInArea(10, 0, 10),
                "Точка на границе четверти окружности должна быть внутри.");
        assertTrue(AreaChecker.isInArea(7.07, -7.07, 10),
                "Точка на границе четверти окружности должна быть внутри.");
        assertFalse(AreaChecker.isInArea(10.1, -0.1, 10),
                "Точка вне границы четверти окружности должна быть снаружи.");

        // Граничные условия для треугольника (второй квадрант)
        assertTrue(AreaChecker.isInArea(0, 0, 10),
                "Вершина треугольника должна быть внутри.");
        assertTrue(AreaChecker.isInArea(-5, 0, 10),
                "Вершина треугольника (-R/2, 0) должна быть внутри.");
        assertTrue(AreaChecker.isInArea(0, 10, 10),
                "Вершина треугольника (0, R) должна быть внутри.");
        assertFalse(AreaChecker.isInArea(-5.1, 0, 10),
                "Точка вне треугольника должна быть снаружи.");

        // Граничные условия для прямоугольника (первый квадрант)
        assertTrue(AreaChecker.isInArea(10, 10, 10),
                "Точка на границе прямоугольника должна быть внутри.");
        assertTrue(AreaChecker.isInArea(0, 10, 10),
                "Точка на границе прямоугольника должна быть внутри.");
        assertFalse(AreaChecker.isInArea(10.1, 5, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");
        assertFalse(AreaChecker.isInArea(5, 10.1, 10),
                "Точка вне границ прямоугольника должна быть снаружи.");
    }
}
