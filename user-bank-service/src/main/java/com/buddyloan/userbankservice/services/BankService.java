package com.buddyloan.userbankservice.services;

import java.util.List;
import java.util.Optional;

import com.buddyloan.userbankservice.domain.Bank;

public interface BankService {
    Bank save(Bank bank);

    List<Bank> findAll();

    Optional<Bank> findById(int accountNum );

    void deleteById( int accountNum);

    List<Bank> findByaadhar(String id);

    Optional<Bank> findBybankName(String id, String name);
    
}
