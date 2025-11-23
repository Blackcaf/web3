package org.example.mappers;

import org.example.dto.ResultDTO;
import org.example.entities.ResultEntity;

public class ResultMapper {

    public static ResultDTO toDTO(ResultEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new ResultDTO(
            entity.getId(),
            entity.getX(),
            entity.getY(),
            entity.getR(),
            entity.getHit(),
            entity.getTimestamp(),
            entity.getExecutionTime()
        );
    }

    public static ResultEntity toEntity(ResultDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ResultEntity entity = new ResultEntity(
            dto.getX(),
            dto.getY(),
            dto.getR(),
            dto.getHit(),
            dto.getExecutionTime()
        );
        entity.setId(dto.getId());
        entity.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : java.time.LocalDateTime.now());

        return entity;
    }

    public static ResultEntity toEntityForSave(ResultDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new ResultEntity(
            dto.getX(),
            dto.getY(),
            dto.getR(),
            dto.getHit(),
            dto.getExecutionTime()
        );
    }
}

