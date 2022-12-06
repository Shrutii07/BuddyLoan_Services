package com.buddyloan.userloanservice.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.buddyloan.userloanservice.domain.History;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class LoanDto {

    private int loanId;

    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number.")
    private String aadhaarId;

    @NotBlank()
    @NotNull
    private String loanType;

    @Min(1)
    @NotNull
    private double loanAmount;

   
    private double emi;

    @Min(1)
    @NotNull
    private int duration;

    
    private double totalPayableAmount;


    @Min(0)
    @NotNull
    private double interestRate;

    private LocalDate appliedDate;

    private LocalDate dueDate;  

    //private Boolean status = true;

    // DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-mm-dd");

    // @Pattern(regexp="^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid Date format - yyyy-mm-dd")
    // private String applyDate= appliedDate.format(formatters);

    private List<History> history;
}
