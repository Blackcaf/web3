package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.dto.StatisticsDTO;
import org.example.entities.ResultEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;

@ApplicationScoped
public class StatisticsService {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);

    private final Map<Integer, AtomicLong> xFrequency = new ConcurrentHashMap<>();
    private final Map<Double, AtomicLong> rFrequency = new ConcurrentHashMap<>();

    private final DoubleAdder sumX = new DoubleAdder();
    private final DoubleAdder sumY = new DoubleAdder();
    private final DoubleAdder sumR = new DoubleAdder();
    private final AtomicLong totalExecutionTimeNs = new AtomicLong(0);

    /**
     * Обрабатывает один результат (вызывается из Kafka Consumer)
     */
    public void processResult(ResultEntity entity) {
        if (entity == null) {
            return;
        }

        totalRequests.incrementAndGet();

        if (Boolean.TRUE.equals(entity.getHit())) {
            hitCount.incrementAndGet();
        } else {
            missCount.incrementAndGet();
        }

        if (entity.getX() != null) {
            sumX.add(entity.getX());
            xFrequency.computeIfAbsent(entity.getX(), k -> new AtomicLong(0)).incrementAndGet();
        }

        if (entity.getY() != null) {
            sumY.add(entity.getY());
        }

        if (entity.getR() != null) {
            sumR.add(entity.getR());
            rFrequency.computeIfAbsent(entity.getR(), k -> new AtomicLong(0)).incrementAndGet();
        }

        addExecutionTime(entity.getExecutionTime());

        System.out.println("StatisticsService: Processed - Total=" + totalRequests.get() +
                ", Hits=" + hitCount.get() + ", Misses=" + missCount.get());
    }

    /**
     * Пересчитывает статистику из списка результатов (при старте приложения)
     */
    public synchronized void recalculateFromDatabase(List<ResultEntity> results) {
        System.out.println("=== StatisticsService: Recalculating from database ===");

        // Сбрасываем текущие значения
        reset();

        // Пересчитываем все результаты
        for (ResultEntity entity : results) {
            processResultSilently(entity);
        }

        System.out.println("StatisticsService: Recalculated from " + results.size() + " records");
        System.out.println("  Total=" + totalRequests.get() +
                ", Hits=" + hitCount.get() +
                ", Misses=" + missCount.get());
    }

    /**
     * Обрабатывает результат без логирования (для массовой загрузки)
     */
    private void processResultSilently(ResultEntity entity) {
        if (entity == null) {
            return;
        }

        totalRequests.incrementAndGet();

        if (Boolean.TRUE.equals(entity.getHit())) {
            hitCount.incrementAndGet();
        } else {
            missCount.incrementAndGet();
        }

        if (entity.getX() != null) {
            sumX.add(entity.getX());
            xFrequency.computeIfAbsent(entity.getX(), k -> new AtomicLong(0)).incrementAndGet();
        }

        if (entity.getY() != null) {
            sumY.add(entity.getY());
        }

        if (entity.getR() != null) {
            sumR.add(entity.getR());
            rFrequency.computeIfAbsent(entity.getR(), k -> new AtomicLong(0)).incrementAndGet();
        }

        addExecutionTime(entity.getExecutionTime());
    }

    /**
     * Возвращает текущую статистику
     */
    public StatisticsDTO getStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        long total = totalRequests.get();
        long hits = hitCount.get();
        long misses = missCount.get();

        stats.setTotalRequests(total);
        stats.setHitCount(hits);
        stats.setMissCount(misses);

        if (total > 0) {
            stats.setHitRate((hits * 100.0) / total);
            stats.setAverageX(sumX.sum() / total);
            stats.setAverageY(sumY.sum() / total);
            stats.setAverageR(sumR.sum() / total);
            stats.setAverageExecutionTimeMs((totalExecutionTimeNs.get() / 1_000_000.0) / total);
        }

        stats.setMostFrequentX(findMostFrequent(xFrequency));
        stats.setMostFrequentR(findMostFrequentDouble(rFrequency));
        stats.setTotalExecutionTimeNs(totalExecutionTimeNs.get());

        return stats;
    }

    /**
     * Сбрасывает всю статистику
     */
    public synchronized void reset() {
        totalRequests.set(0);
        hitCount.set(0);
        missCount.set(0);
        xFrequency.clear();
        rFrequency.clear();
        sumX.reset();
        sumY.reset();
        sumR.reset();
        totalExecutionTimeNs.set(0);
        System.out.println("StatisticsService: Reset completed");
    }

    // === Private helper methods ===

    private void addExecutionTime(String execTime) {
        if (execTime == null || execTime.isEmpty()) {
            return;
        }
        try {
            String cleaned = execTime
                    .replace("мс", "")
                    .replace("ms", "")
                    .replace(",", ".")
                    .trim();
            double timeMs = Double.parseDouble(cleaned);
            totalExecutionTimeNs.addAndGet((long) (timeMs * 1_000_000));
        } catch (NumberFormatException ignored) {
        }
    }

    private Integer findMostFrequent(Map<Integer, AtomicLong> map) {
        return map.entrySet().stream()
                .max((a, b) -> Long.compare(a.getValue().get(), b.getValue().get()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private Double findMostFrequentDouble(Map<Double, AtomicLong> map) {
        return map.entrySet().stream()
                .max((a, b) -> Long.compare(a.getValue().get(), b.getValue().get()))
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}