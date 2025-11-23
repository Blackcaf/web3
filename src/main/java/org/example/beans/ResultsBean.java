package org.example.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dto.ResultDTO;
import org.example.service.ResultService;

import java.io.Serializable;
import java.util.List;

@Named("resultsBean")
@SessionScoped
public class ResultsBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ResultService resultService;
    
    @PostConstruct
    public void init() {
    }

    public void addResult(ResultDTO resultDTO) {
        resultService.addResult(resultDTO);
    }

    public List<ResultDTO> getResults() {
        return resultService.getAllResults();
    }

    public void clearResults() {
        resultService.clearAllResults();
    }
}

