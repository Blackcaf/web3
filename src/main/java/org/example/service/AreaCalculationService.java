package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AreaCalculationService {
    
    public boolean checkPoint(Integer x, Double y, Double r) {
        if (x == null || y == null || r == null || r <= 0) {
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

