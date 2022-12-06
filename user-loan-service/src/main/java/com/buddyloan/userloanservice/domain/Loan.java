package com.buddyloan.userloanservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@Table(name = "LoanDetails")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loanId_generator")
    @SequenceGenerator(name="loanId_generator",initialValue = 11, allocationSize = 1, sequenceName = "loanId_seq")
    @Column(name="loanId", updatable = false, nullable = false)
    private int loanId;
    @Column(name="aadhaarId", nullable = false)
    private String aadhaarId;
    @Column(name = "loanType", nullable = false)
    private String loanType;
    @Column(name = "loanAmount", nullable = false)
    private double loanAmount;
    @Column(name = "emi", nullable = false)
    private double emi;
    @Column(name = "duration", nullable = false)
    private int duration;
    @Column(name = "interestRate", nullable = false)
    private double interestRate;
    @Column(name = "dueDate", nullable = false)
    private LocalDate dueDate;

    @Column(name = "appliedDate", nullable = false)
    private LocalDate appliedDate = LocalDate.now();
   
    @Column(name="totalPayableAmount", nullable = false, updatable = false)
    private double totalPayableAmount;

    // @Column(name="status", nullable = true)
    // private Boolean status;

    @OneToMany(targetEntity = History.class, cascade = CascadeType.ALL)
    @JoinColumn(name="loan_Id", referencedColumnName = "loanId")
    private List<History> history;

}
