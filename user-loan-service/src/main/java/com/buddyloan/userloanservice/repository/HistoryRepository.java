package com.buddyloan.userloanservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buddyloan.userloanservice.domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findAllByOrderByDueMonthDateDesc();


}
