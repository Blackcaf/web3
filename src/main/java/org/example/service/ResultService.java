package org.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.ResultDTO;
import org.example.entities.ResultEntity;
import org.example.mappers.ResultMapper;
import org.example.repository.ResultRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ResultService {
    
    @Inject
    private ResultRepository repository;
    
    public void addResult(ResultDTO resultDTO) {
        try {
            ResultEntity entity = ResultMapper.toEntityForSave(resultDTO);
            repository.save(entity);
            
            resultDTO.setId(entity.getId());
            resultDTO.setTimestamp(entity.getTimestamp());
            
            System.out.println("ResultService: Result saved to DB, ID: " + entity.getId());
        } catch (Exception e) {
            System.err.println("ResultService: Error adding result: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add result to database", e);
        }
    }
    
    public List<ResultDTO> getAllResults() {
        try {
            List<ResultEntity> entities = repository.findAll();
            List<ResultDTO> results = entities.stream()
                    .map(ResultMapper::toDTO)
                    .collect(Collectors.toList());
            
            System.out.println("ResultService: Retrieved " + results.size() + " results from database");
            return results;
        } catch (Exception e) {
            System.err.println("ResultService: Error getting all results: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    public void clearAllResults() {
        try {
            repository.deleteAll();
            System.out.println("ResultService: All results cleared from DB");
        } catch (Exception e) {
            System.err.println("ResultService: Error clearing all results: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to clear all results", e);
        }
    }
}

