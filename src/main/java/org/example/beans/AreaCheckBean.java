package org.example.beans;

import jakarta.enterprise.event.Event;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.dto.ResultDTO;
import org.example.event.ResultAddedEvent;
import org.example.service.ResultService;

import java.io.Serializable;

@Named("areaCheckBean")
@ViewScoped
public class AreaCheckBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ResultService resultService;

    @Inject
    private Event<ResultAddedEvent> resultAddedEvent;

    private Integer x;
    private Double y;
    private Double r = 1.0;

    public String checkPoint() {
        if (x == null || y == null || r == null) {
            return null;
        }

        ResultDTO result = resultService.checkAndSave(x, y, r);
        resultAddedEvent.fire(new ResultAddedEvent(result.getId()));

        return null;
    }

    public Integer getX() {return x;}
    public void setX(Integer x) {this.x = x;}
    public Double getY() {return y;}
    public void setY(Double y) {this.y = y;}
    public Double getR() {return r;}
    public void setR(Double r) {this.r = r;}
}