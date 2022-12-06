package com.buddyloan.userloanservice.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.buddyloan.userloanservice.domain.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer>{
    
    List<Loan> findByAadhaarIdOrderByDueDateAsc(String id);

    List<Loan> findByAadhaarId(String id);
}
