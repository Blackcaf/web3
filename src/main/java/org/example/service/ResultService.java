package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.dto.ResultDTO;
import org.example.entities.ResultEntity;
import org.example.repository.ResultRepository;
import org.example.util.AreaCheckUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ResultService {

    private static final String TIME_UNIT = "ms";

    @Inject
    private ResultRepository repository;

    @Inject
    private ResultEventPublisher eventPublisher;  // Интерфейс вместо конкретного класса

    public ResultDTO checkAndSave(Integer x, Double y, Double r) {
        long startTime = System.nanoTime();
        boolean hit = AreaCheckUtil.checkPoint(x, y, r);
        long endTime = System.nanoTime();

        String executionTime = formatExecutionTime(endTime - startTime);
        ResultDTO dto = new ResultDTO(x, y, r, hit, executionTime);
        ResultEntity entity = dto.toEntity();
        ResultEntity savedEntity = repository.save(entity);
        dto.setId(savedEntity.getId());
        dto.setTimestamp(savedEntity.getTimestamp());
        eventPublisher.publish(savedEntity);
        System.out.println("ResultService: Saved ID=" + savedEntity.getId() + ", hit=" + hit);

        return dto;
    }

    public List<ResultDTO> findAll() {
        List<ResultEntity> entities = repository.findAll();
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(ResultDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void clearAll() {
        repository.deleteAll();
        System.out.println("ResultService: All results cleared");
    }

    public long count() {
        return repository.count();
    }

    private String formatExecutionTime(long nanos) {
        double millis = nanos / 1_000_000.0;
        return String.format("%.3f %s", millis, TIME_UNIT);
    }
}