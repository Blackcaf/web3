package com.nlshakal.jsfgraph.db;

import com.nlshakal.jsfgraph.entity.ResultEntity;

import java.util.Collection;

public interface ResultDAO {
    void addNewResult(ResultEntity result);

    void updateResult(Long result_id, ResultEntity result);

    ResultEntity getResultById(Long result_id);

    Collection<ResultEntity> getAllResults();

    void deleteResult(ResultEntity result);

    void clearResults();
}

