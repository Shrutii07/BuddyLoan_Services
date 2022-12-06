package com.buddyloan.userloanservice.services;

import java.util.List;
import java.util.Optional;

import com.buddyloan.userloanservice.domain.Loan;

public interface LoanServices {

    Loan save(Loan loan);

    List<Loan> findAll();

    Optional<Loan> findById(int id);

    void deleteById( int id);

    List<Loan> orderByDueDate(String id);

    List<Loan> findByAadhar(String id);

}
