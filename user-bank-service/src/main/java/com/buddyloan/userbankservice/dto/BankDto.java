package com.buddyloan.userbankservice.dto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class BankDto {

    @NotBlank(message = "Bank Name is required")
    @NotNull
    private String bankName;
    
    @NotNull
    private int accountNum;

    //@Pattern(regexp = "^[0-9]{6}$", message = "Account number should be of 6 digits.")
    //@NotNull
    //@Size(min=6,max=6)
    //private String accNum = String.valueOf(accountNum);

    @NotNull(message="Bank balance cant be null")
    private double bankBalance;

    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number.")
    private String aadhaarId;

    @NotNull(message="Bank balance cant be null")
    private int pin;
}
