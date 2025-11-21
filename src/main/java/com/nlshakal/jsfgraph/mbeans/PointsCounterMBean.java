package com.nlshakal.jsfgraph.mbeans;

public interface PointsCounterMBean {
    int getTotalPoints();
    int getMissedPoints();
    void incrementTotalPoints();
    void incrementMissedPoints();
    void resetConsecutiveMisses();
    void resetAndInitializeCounts();
}