package org.example.event;

public class ResultAddedEvent {

    private final Long resultId;

    public ResultAddedEvent() {this.resultId = null;}

    public ResultAddedEvent(Long resultId) {this.resultId = resultId;}

    public Long getResultId() {return resultId;}
}