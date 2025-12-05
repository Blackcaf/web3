package org.example.config;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.entities.ResultEntity;
import org.example.repository.ResultRepository;
import org.example.service.StatisticsService;

import java.util.List;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Inject
    private ResultRepository resultRepository;

    @Inject
    private StatisticsService statisticsService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== Application Started ===");
        recalculateStatisticsFromDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== Application Stopping ===");
    }

    private void recalculateStatisticsFromDatabase() {
        try {
            List<ResultEntity> allResults = resultRepository.findAll();

            if (allResults != null && !allResults.isEmpty()) {
                statisticsService.recalculateFromDatabase(allResults);
                System.out.println("Statistics restored from " + allResults.size() + " records");
            } else {
                System.out.println("No records in database, statistics start from zero");
            }
        } catch (Exception e) {
            System.err.println("Failed to recalculate statistics: " + e.getMessage());
        }
    }
}