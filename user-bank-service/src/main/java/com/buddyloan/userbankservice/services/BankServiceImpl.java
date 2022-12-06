package com.buddyloan.userbankservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.buddyloan.userbankservice.domain.Bank;
import com.buddyloan.userbankservice.repository.BankRepository;

@Service
public class BankServiceImpl implements BankService {

    private BankRepository  bankRepository;

    public BankServiceImpl(BankRepository repo){
        this.bankRepository = repo;
    }


    @Override
    public Bank save(Bank bank) {
        return bankRepository.save(bank);
    }

    @Override
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }

    @Override
    public Optional<Bank> findById(int accountNum) {
        return bankRepository.findById(accountNum);
    }

    @Override
    public void deleteById(int accountNum) {
        bankRepository.deleteById(accountNum);
    }

    @Override
    public List<Bank> findByaadhar(String id){
        return bankRepository.findByAadhaarId(id);
    }


    @Override
    public Optional<Bank> findBybankName(String id, String name){
        return bankRepository.findByAadhaarIdAndBankName(id,name);
    }
    
}
