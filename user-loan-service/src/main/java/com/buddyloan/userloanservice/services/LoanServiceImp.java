package com.buddyloan.userloanservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.buddyloan.userloanservice.domain.Loan;
import com.buddyloan.userloanservice.repository.LoanRepository;



@Service
public class LoanServiceImp implements LoanServices {
    private LoanRepository loanRepository;
    public LoanServiceImp(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan){
        return this.loanRepository.save(loan);
    }

    @Override
    public List<Loan> findAll(){
        return this.loanRepository.findAll();
    }


    @Override
    public Optional<Loan> findById(int id){
        return loanRepository.findById(id);
    }

    @Override
    public void deleteById(int id) {
        loanRepository.deleteById(id);
    }

    @Override
    public List<Loan> orderByDueDate(String id) {
        return loanRepository.findByAadhaarIdOrderByDueDateAsc(id);
    }

    @Override
    public List<Loan> findByAadhar(String id){
        return loanRepository.findByAadhaarId(id);
    }

    
}
