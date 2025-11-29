package org.example.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "results")
public class ResultEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double r;

    @Column(nullable = false)
    private Boolean hit;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String executionTime;

    public ResultEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public ResultEntity(Integer x, Double y, Double r, Boolean hit, String executionTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.executionTime = executionTime;
        this.timestamp = LocalDateTime.now();
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
        ResultEntity that = (ResultEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", hit=" + hit +
                ", timestamp=" + timestamp +
                '}';
    }
}