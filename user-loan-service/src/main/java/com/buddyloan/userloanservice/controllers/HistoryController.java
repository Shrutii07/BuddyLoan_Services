package com.buddyloan.userloanservice.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.buddyloan.userloanservice.dto.HistoryDto;
import com.buddyloan.userloanservice.services.HistoryService;
import com.buddyloan.userloanservice.services.LoanServices;

@RestController
public class HistoryController {

    @Autowired
    private LoanServices loanservice;

    @Autowired
    private HistoryService historyService;
    
    public ResponseEntity<List<History>> getAllHistory(){
        return ResponseEntity.ok().body(historyService.findAll());
    }

    @GetMapping("/history/{transactionId}")
    public ResponseEntity<?> getHistoryById(@PathVariable int transactionId){
        Optional<History> transaction = historyService.findById(transactionId);
        if (transaction.isPresent()) {
            return ResponseEntity.ok().body(transaction.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/history/{transactionId}")
    public ResponseEntity<?> updateTranscationById(@PathVariable int transactionId, @RequestBody HistoryDto historyDto){
        Optional<History> transaction = historyService.findById(transactionId);
        if(transaction.isPresent()){
            transaction.get().setAmountPaid(historyDto.getAmountPaid());
            transaction.get().setDueMonthDate(historyDto.getDueMonthDate());
            transaction.get().setDatePaidOn(historyDto.getDatePaidOn());
            transaction.get().setIsFullyPaid(historyDto.getIsFullyPaid());
            return ResponseEntity.status(HttpStatus.CREATED).body(historyService.save(transaction.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/history/{transactionId}")
    public ResponseEntity<?> deleteTransactionById(@PathVariable int transactionId) {
        historyService.deleteById(transactionId);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    @GetMapping("loans/{loanId}/history")
    public ResponseEntity<?> getHistoryByLoanId(@PathVariable int loanId){
        Optional<Loan> loan = loanservice.findById(loanId);
        if(loan.isPresent()){
            return ResponseEntity.ok().body(loan.get().getHistory());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("customers/{aadharId}/history")
    public ResponseEntity<?> getAllHistoryByCustomerId(@PathVariable String aadharId){
        List<Loan>  loans = loanservice.findByAadhar(aadharId);
        if(loans.size()>0){
                List<History> history = new ArrayList<>();
                for (Loan loan:loans) {
                    if(loan.getHistory().size()>0){
                        for (History transaction: loan.getHistory()) {
                            history.add(transaction);
                        }
                    }
                }
                
                return ResponseEntity.ok().body(history);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Loans Found");
    }

    @PostMapping("loans/{loanId}/history")
    public ResponseEntity<?> addHistoryByLoanId(@PathVariable int loanId,@Valid @RequestBody HistoryDto historyDto){
        Optional<Loan> loan = loanservice.findById(loanId);
        if(!loan.isPresent()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Loan Found");
        }
        List<History> history = loan.get().getHistory();
        History newTransaction = new History();
        if(history.size()==0){
            newTransaction.setDueMonthDate(loan.get().getAppliedDate().plusMonths(1));
        }
        else{
            int numOfTrans = history.size();
            newTransaction.setDueMonthDate(loan.get().getAppliedDate().plusMonths(1+numOfTrans));
        }
        newTransaction.setAmountPaid(historyDto.getAmountPaid());
        newTransaction.setDatePaidOn(LocalDate.now());
        newTransaction.setIsFullyPaid(historyDto.getIsFullyPaid());
        history.add(newTransaction);
        loan.get().setHistory(history);
        loanservice.save(loan.get());
        return ResponseEntity.ok().body(history);
    }

    @GetMapping("/history")
    @ResponseBody
    public ResponseEntity<?> getHistorySorted(@RequestParam (required = false) Integer loanId, @RequestParam (required = false) String id){
        if(id == null && loanId==null){
            return getAllHistory();
        }
        else if (!(id == null ||id.trim().isEmpty())){
            return getHistoryByIdSortedByDueDate(id);
        }
        else if( loanId!=null){
            return getHistoryByLoanSortByDueDate(loanId);
        }
        return getAllHistory();
    }


    public ResponseEntity<?> getHistoryByLoanSortByDueDate(@PathVariable int loanId){
        Optional<Loan> loan = loanservice.findById(loanId);
        if(loan.isPresent()){
            List<History> userHistory = loan.get().getHistory();
            if(userHistory.size()==0){
                return ResponseEntity.ok().body("User has no history of transactions.");
            }
            if(userHistory.size()==1){
                return ResponseEntity.ok().body(userHistory);
            }
            List<History> allHistory = historyService.orderByDueDate();
            List<History> historyListById = new ArrayList<>();

            for(History transaction: allHistory){
                if(userHistory.contains(transaction)){
                    historyListById.add(transaction);
                }
            }
            return ResponseEntity.ok().body(historyListById);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No such loan exists");
    }

    public ResponseEntity<?> getHistoryByIdSortedByDueDate(@PathVariable String Id) {
        List<Loan> loans = loanservice.findByAadhar(Id);
        if(loans.size()>0){
            List<History> history = new ArrayList<>();
                for (Loan loan : loans) {
                    if (loan.getHistory().size() > 0) {
                        for (History transaction : loan.getHistory()) {
                            history.add(transaction);
                        }
                    }
                }
            if (history.size() == 0) return ResponseEntity.ok().body("User has no history of transactions.");
            else if (history.size() == 1) return ResponseEntity.ok().body(history);
            else{
                List<History> allHistory = historyService.orderByDueDate();
                List<History> historyListById = new ArrayList<>();

                for (History transaction : allHistory) {
                        if (history.contains(transaction)) {
                            historyListById.add(transaction);
                        }
                }
                return ResponseEntity.ok().body(historyListById);

            }
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has no loans");
    }

}
