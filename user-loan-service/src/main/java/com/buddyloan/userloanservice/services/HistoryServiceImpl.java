package com.buddyloan.userloanservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.buddyloan.userloanservice.domain.History;
import com.buddyloan.userloanservice.repository.HistoryRepository;

@Service
public class HistoryServiceImpl implements HistoryService {
    private HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public History save(History transcation) {
        return historyRepository.save(transcation);
    }

    @Override
    public List<History> findAll() {
        return historyRepository.findAll();
    }

    @Override
    public Optional<History> findById(int id) {
        return historyRepository.findById(id);
    }

    @Override
    public List<History> orderByDueDate() {
        return historyRepository.findAllByOrderByDueMonthDateDesc();
    }

    @Override
    public void deleteById(int id) {
        historyRepository.deleteById(id);
    }

}
