package org.example.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dto.ResultDTO;
import org.example.service.ResultService;

import java.io.Serializable;
import java.util.List;

@Named("resultsBean")
@ApplicationScoped
public class ResultsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private ResultService resultService;

    @Inject
    @Push(channel = "resultChannel")
    private PushContext pushContext;

    public void init() {
    }

    public void addResult(ResultDTO resultDTO) {
        resultService.addResult(resultDTO);
        pushContext.send("update");
    }

    public List<ResultDTO> getResults() {
        return resultService.getAllResults();
    }

    public void clearResults() {
        resultService.clearAllResults();
        pushContext.send("update");
    }
}