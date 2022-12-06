package com.buddyloan.userloanservice.controllers;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.buddyloan.userloanservice.domain.History;
import com.buddyloan.userloanservice.domain.Loan;
import com.buddyloan.userloanservice.dto.LoanDto;
import com.buddyloan.userloanservice.services.LoanServices;



@RestController
public class LoanController {

    @Autowired
    private LoanServices loanservice;

    @GetMapping("/loans")
    public ResponseEntity<List<Loan>> getAllLoans(){
        return ResponseEntity.ok().body(loanservice.findAll());
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<?> getLoanById(@PathVariable int loanId){
        Optional<Loan> loan = loanservice.findById(loanId);
        if (loan.isPresent()) {
            return ResponseEntity.ok().body(loan.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/loans/{loanId}")
    public ResponseEntity<?> updateLoanById(@PathVariable int loanId, @RequestBody LoanDto loanDto){
        Optional<Loan> loan = loanservice.findById(loanId);
        if(loan.isPresent()){
            loan.get().setLoanAmount(loanDto.getLoanAmount());
            loan.get().setLoanType(loanDto.getLoanType());
            loan.get().setDuration(loanDto.getDuration());
            loan.get().setInterestRate(loanDto.getInterestRate());
            
            loan.get().setDueDate(loan.get().getAppliedDate().plusMonths(loanDto.getDuration()));

            double rate = loanDto.getInterestRate()/12/100;
            double emi = ((loanDto.getLoanAmount()*rate*Math.pow((1+rate), loanDto.getDuration()))/((Math.pow((1+rate), loanDto.getDuration()))-1));
            DecimalFormat df = new DecimalFormat("0.00"); 
            emi = Double.parseDouble(df.format(emi));

            loan.get().setEmi(emi);
            loan.get().setTotalPayableAmount(emi*loanDto.getDuration());
            
            loan.get().setHistory(loanDto.getHistory());
            return ResponseEntity.status(HttpStatus.CREATED).body(loanservice.save(loan.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    @DeleteMapping("/loans/{loanID}")
    public ResponseEntity<?> deleteLoanById(@PathVariable int loanID) {
        loanservice.deleteById(loanID);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    public ResponseEntity<?>  getLoanByCustomerId(@PathVariable String id) {
        List<Loan> loans = loanservice.findByAadhar(id);
        if (loans.size()>0) {
            return ResponseEntity.ok().body(loans);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); 
    }

    @GetMapping("/customers/{id}/loans/{loanId}")
    public ResponseEntity<?>  getLoanByCustomerIdAndLoanId(@PathVariable String id,@PathVariable int loanId) {
        List<Loan> loans = loanservice.findByAadhar(id);
        if (loans.size()>0) {
            Loan loan = loans.stream().filter(a -> a.getLoanId() == loanId).collect(Collectors.toList()).get(0);
            return ResponseEntity.ok().body(loan);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); 
    }


    @PostMapping("/customers/{id}/loans")
    public ResponseEntity<?> postNewBankByCustomerId(@PathVariable String id,@Valid @RequestBody LoanDto loanDto){
        Loan loanActual = new Loan();
        loanActual.setAadhaarId(id);
        loanActual.setDueDate(LocalDate.now().plusMonths(loanDto.getDuration()));
        loanActual.setLoanAmount(loanDto.getLoanAmount());
        loanActual.setDuration(loanDto.getDuration());
        loanActual.setInterestRate(loanDto.getInterestRate());
        loanActual.setAppliedDate(LocalDate.now());
        loanActual.setLoanType(loanDto.getLoanType());
        
        double rate = loanDto.getInterestRate()/12/100;
        double emi = ((loanDto.getLoanAmount()*rate*Math.pow((1+rate), loanDto.getDuration()))/((Math.pow((1+rate), loanDto.getDuration()))-1));
        DecimalFormat df = new DecimalFormat("0.00"); 
        emi = Double.parseDouble(df.format(emi));

        loanActual.setEmi(emi);
        loanActual.setTotalPayableAmount(Double.parseDouble(df.format(emi*loanDto.getDuration())));
        return ResponseEntity.status(HttpStatus.CREATED).body(loanservice.save(loanActual));
    }

    @GetMapping("customers/{id}/loans")
    @ResponseBody
    public ResponseEntity<?> getLoans(@RequestParam (required = false) String sort, @PathVariable String id){
        if(sort == null) return getLoanByCustomerId(id);
        else if(sort.equalsIgnoreCase("dueDate")){
            return getLoansSortedByDueDate(id);
        }
        else if(sort.equalsIgnoreCase("complete")){
            return getCompletedLoans(id);
        }
        else return getLoanByCustomerId(id);
    }


    public ResponseEntity<?> getLoansSortedByDueDate(@PathVariable String id){
        List<Loan> loans = loanservice.orderByDueDate(id);
        List<Loan> validLoans = new ArrayList<>();
        if (loans.size()>0) {
            for(Loan loan: loans){
                if(!checkifLoanCompleted(loan.getLoanId())){
                    validLoans.add(loan);
                }
            }
            return ResponseEntity.ok().body(validLoans);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);    
    }

    
    public ResponseEntity<?> getCompletedLoans(@PathVariable String id){
        List<Loan> loans = loanservice.orderByDueDate(id);
        List<Loan> validLoans = new ArrayList<>();
        if (loans.size()>0) {
            for(Loan loan: loans){
                if(checkifLoanCompleted(loan.getLoanId())){
                    validLoans.add(loan);
                }
            }
            return ResponseEntity.ok().body(validLoans);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);    
    }

    @GetMapping("/loans/{loanId}/isComplete")
    public Boolean checkifLoanCompleted(@PathVariable int loanId){
        Optional<Loan> loan = loanservice.findById(loanId);
        if (loan.isPresent()) {
            double totalAmount = loan.get().getTotalPayableAmount();
            double amountPaid = 0;
            List<History> history= loan.get().getHistory();
            if(history.isEmpty()) return false;

            for (History transaction : history ) {
                if(transaction.getIsFullyPaid()) return true;
                amountPaid += transaction.getAmountPaid();
            }

            if(totalAmount - amountPaid < 2) return true;
            return false;
        }
        return false;    
    }

    @GetMapping("/loans/{loanId}/getRemaining")
    public Double getRemainingAmount(@PathVariable int loanId){
        Optional<Loan> loan = loanservice.findById(loanId);
        if (loan.isPresent()) {
            double totalAmount = loan.get().getLoanAmount();
            double amountPaid = 0;
            List<History> history= loan.get().getHistory();
            if(history.isEmpty()) return totalAmount;
            if(checkifLoanCompleted(loanId)) return 0.0;
            for (History transaction : history ) {
                amountPaid += transaction.getAmountPaid();
            }
            return totalAmount - amountPaid;
        }
        return -1.0;    
    }


}
