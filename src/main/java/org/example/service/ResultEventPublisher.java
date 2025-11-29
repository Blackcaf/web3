package org.example.service;

import org.example.entities.ResultEntity;

public interface ResultEventPublisher {

    void publish(ResultEntity result);
}