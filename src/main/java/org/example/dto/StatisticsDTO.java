package org.example.dto;

import java.io.Serializable;

public class StatisticsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long totalRequests;
    private long hitCount;
    private long missCount;
    private double hitRate;
    private double averageX;
    private double averageY;
    private double averageR;
    private Integer mostFrequentX;
    private Double mostFrequentR;
    private long totalExecutionTimeNs;
    private double averageExecutionTimeMs;

    public StatisticsDTO() {}

    public long getTotalRequests() { return totalRequests; }
    public long getHitCount() { return hitCount; }
    public long getMissCount() { return missCount; }
    public double getHitRate() { return hitRate; }
    public double getAverageX() { return averageX; }
    public double getAverageY() { return averageY; }
    public double getAverageR() { return averageR; }
    public Integer getMostFrequentX() { return mostFrequentX; }
    public Double getMostFrequentR() { return mostFrequentR; }
    public long getTotalExecutionTimeNs() { return totalExecutionTimeNs; }
    public double getAverageExecutionTimeMs() { return averageExecutionTimeMs; }

    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public void setHitCount(long hitCount) { this.hitCount = hitCount; }
    public void setMissCount(long missCount) { this.missCount = missCount; }
    public void setHitRate(double hitRate) { this.hitRate = hitRate; }
    public void setAverageX(double averageX) { this.averageX = averageX; }
    public void setAverageY(double averageY) { this.averageY = averageY; }
    public void setAverageR(double averageR) { this.averageR = averageR; }
    public void setMostFrequentX(Integer mostFrequentX) { this.mostFrequentX = mostFrequentX; }
    public void setMostFrequentR(Double mostFrequentR) { this.mostFrequentR = mostFrequentR; }
    public void setTotalExecutionTimeNs(long totalExecutionTimeNs) { this.totalExecutionTimeNs = totalExecutionTimeNs; }
    public void setAverageExecutionTimeMs(double averageExecutionTimeMs) { this.averageExecutionTimeMs = averageExecutionTimeMs; }
}