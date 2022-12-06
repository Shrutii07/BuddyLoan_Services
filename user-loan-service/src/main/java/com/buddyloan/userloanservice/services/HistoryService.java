package com.buddyloan.userloanservice.services;

import java.util.List;
import java.util.Optional;

import com.buddyloan.userloanservice.domain.History;

public interface HistoryService {
    History save(History transcation);

    List<History> findAll();

    Optional<History> findById(int id);

    List<History> orderByDueDate();
    void deleteById( int id);

}
