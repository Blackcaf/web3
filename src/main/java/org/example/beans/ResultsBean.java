package org.example.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dto.ResultDTO;
import org.example.event.ResultAddedEvent;
import org.example.service.ResultService;
import org.example.service.StatisticsService;

import java.io.Serializable;
import java.util.List;

@Named("resultsBean")
@ApplicationScoped
public class ResultsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private ResultService resultService;

    @Inject
    private StatisticsService statisticsService;

    @Inject
    @Push(channel = "resultChannel")
    private PushContext pushContext;

    public List<ResultDTO> getResults() {
        return resultService.findAll();
    }

    public void onResultAdded(@Observes ResultAddedEvent event) {
        System.out.println("ResultsBean: Received ResultAddedEvent");
        pushContext.send("update");
    }

    public void clearResults() {
        resultService.clearAll();
        statisticsService.reset();
        pushContext.send("update");
    }
}