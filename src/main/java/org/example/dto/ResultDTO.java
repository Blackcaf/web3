package org.example.dto;

import org.example.entities.ResultEntity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer x;
    private Double y;
    private Double r;
    private Boolean hit;
    private LocalDateTime timestamp;
    private String executionTime;

    public ResultDTO() {}

    public ResultDTO(Integer x, Double y, Double r, Boolean hit, String executionTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.executionTime = executionTime;
        this.timestamp = LocalDateTime.now();
    }

    public static ResultDTO fromEntity(ResultEntity entity) {
        if (entity == null) return null;

        ResultDTO dto = new ResultDTO();
        dto.setId(entity.getId());
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        dto.setR(entity.getR());
        dto.setHit(entity.getHit());
        dto.setTimestamp(entity.getTimestamp());
        dto.setExecutionTime(entity.getExecutionTime());
        return dto;
    }

    public ResultEntity toEntity() {
        return new ResultEntity(this.x, this.y, this.r, this.hit, this.executionTime);
    }

    public Long getId() { return id; }
    public Integer getX() { return x; }
    public Double getY() { return y; }
    public Double getR() { return r; }
    public Boolean getHit() { return hit; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getExecutionTime() { return executionTime; }

    public void setId(Long id) { this.id = id; }
    public void setX(Integer x) { this.x = x; }
    public void setY(Double y) { this.y = y; }
    public void setR(Double r) { this.r = r; }
    public void setHit(Boolean hit) { this.hit = hit; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultDTO resultDTO = (ResultDTO) o;
        return Objects.equals(id, resultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}