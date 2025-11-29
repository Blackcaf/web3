package org.example.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dto.StatisticsDTO;
import org.example.service.StatisticsService;

import java.io.Serializable;

@Named("statisticsBean")
@ApplicationScoped
public class StatisticsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private StatisticsService statisticsService;

    public StatisticsDTO getStatistics() {
        return statisticsService.getStatistics();
    }

    public void resetStatistics() {
        statisticsService.reset();
    }

    public String formatPercentage(double value) {
        return String.format("%.2f%%", value);
    }

    public String formatDouble(Double value) {
        return value != null ? String.format("%.3f", value) : "N/A";
    }

    public String formatDouble(double value) {
        return String.format("%.3f", value);
    }
}