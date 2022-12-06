package com.buddyloan.userbankservice.repository;

import com.buddyloan.userbankservice.domain.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository  extends JpaRepository<Bank, Integer> {

    List<Bank> findByAadhaarId(String id);

    Optional<Bank> findByAadhaarIdAndBankName(String id, String name);
    
}

