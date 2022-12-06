package com.buddyloan.userbankservice.controllers;

import java.util.ArrayList;
import java.util.Collections;
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

import com.buddyloan.userbankservice.domain.Bank;
import com.buddyloan.userbankservice.dto.BankDto;
import com.buddyloan.userbankservice.services.BankService;

@RestController
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("/banks")
    public ResponseEntity<List<Bank>> getAllBankDetails(){
        return ResponseEntity.ok().body(bankService.findAll());
    }

    @GetMapping("/banks/{accountNum}")
    public ResponseEntity<?> getBankById(@PathVariable int accountNum){
        Optional<Bank> bank = bankService.findById(accountNum);
        if (bank.isPresent()) {
            return ResponseEntity.ok().body(bank.get());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    
    public ResponseEntity<?> updateBankById(@PathVariable int accountNum, @Valid @RequestBody BankDto BankDto){
        Optional<Bank> bankActual = bankService.findById(accountNum);
        if(bankActual.isPresent()){
            bankActual.get().setBankBalance(BankDto.getBankBalance());
            bankActual.get().setAccountNum(BankDto.getAccountNum());
            bankActual.get().setPin(BankDto.getPin());
            bankActual.get().setBankName(BankDto.getBankName());
            return ResponseEntity.status(HttpStatus.CREATED).body(bankService.save(bankActual.get()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/banks/{accountNum}")
    public ResponseEntity<?> deleteBankById(@PathVariable int accountNum) {
        bankService.deleteById(accountNum);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    
    public ResponseEntity<?> getBanksByAadhaarId(@PathVariable String id){
        List<Bank> banks = bankService.findByaadhar(id);
        if(banks.size() >0){
            return ResponseEntity.ok().body(banks);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has no bank details");
    }

    @GetMapping("/customers/{id}/banks/{accNum}")
    public ResponseEntity<?>  getBankByCustomerIdAndAccNum(@PathVariable String id,@PathVariable int accNum) {
        List<Bank> banks = bankService.findByaadhar(id);
        if(banks.size() >0){
                Bank bank = banks.stream().filter(a -> a.getAccountNum() == accNum).collect(Collectors.toList()).get(0);
                return ResponseEntity.ok().body(bank);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User has no bank details");
    }


    @PostMapping("/customers/{id}/banks")
    public ResponseEntity<?> postNewBankByCustomerId(@PathVariable String id, @Valid @RequestBody BankDto bankDto){
        Bank bankActual = new Bank();
        bankActual.setAadhaarId(id);
        bankActual.setAccountNum(bankDto.getAccountNum());
        bankActual.setBankBalance(bankDto.getBankBalance());
        bankActual.setBankName(bankDto.getBankName());
        bankActual.setPin(bankDto.getPin());
        return ResponseEntity.status(HttpStatus.CREATED).body(bankService.save(bankActual));
    }

    @PutMapping("/banks/{accountNum}")
    @ResponseBody
    public ResponseEntity<?> putInBank(@PathVariable int accountNum, @RequestBody BankDto bankDto, @RequestParam (required = false) Double pay) {
        if(pay == null){
            return updateBankById(accountNum, bankDto);
        }
        else{
            Optional<Bank> bankActual = bankService.findById(accountNum);
            if(bankActual.isPresent()){
                bankActual.get().setBankBalance(pay);
                return ResponseEntity.status(HttpStatus.CREATED).body(bankService.save(bankActual.get()));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/customers/{id}/banks")
    @ResponseBody
    public  ResponseEntity<?>  getBankbyBankName(@PathVariable String id, @RequestParam (required = false) String name){
        if(name == null){
            return getBanksByAadhaarId(id);
        }
        else{
            return ResponseEntity.status(HttpStatus.CREATED).body(bankService.findBybankName(id, name));
        }
    }


    @GetMapping("/customers/{id}/balance")
    @ResponseBody
    public Double getBankBalance(@PathVariable String id, @RequestParam (required = false) String get){
        List<Bank> banks = bankService.findByaadhar(id);
        List<Double> amountInBanks =new ArrayList<>();
        if(banks.size() >0){
            for(Bank bank: banks){
                amountInBanks.add(bank.getBankBalance());
            }
        }
        if (get == null){
            return amountInBanks.stream().collect(Collectors.summingDouble(Double::doubleValue));
        }
        else if(get.equalsIgnoreCase("highest")){
            return Collections.max(amountInBanks);
        }
        else if(get.equalsIgnoreCase("lowest")){
            return Collections.min(amountInBanks);
        }
        return amountInBanks.stream().collect(Collectors.summingDouble(Double::doubleValue));
    }

  

}
