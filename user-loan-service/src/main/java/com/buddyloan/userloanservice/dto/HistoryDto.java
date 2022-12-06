package com.buddyloan.userloanservice.dto;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class HistoryDto {
    
    private int transactionId;

    @Min(1)
    @NotNull
    private double amountPaid;

    private LocalDate datePaidOn;
    private LocalDate dueMonthDate;

    @NotNull
    private Boolean isFullyPaid;

}
