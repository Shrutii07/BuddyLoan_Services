package com.buddyloan.userloanservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@Table(name="history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionId_generator")
    @SequenceGenerator(name="transactionId_generator",initialValue = 11000000, allocationSize = 1, sequenceName = "transId_seq")
    @Column(name="transactionId", updatable = false, nullable = false)
    private int transactionId;

    @Column(name="amountPaid", nullable = false)
    private double amountPaid;

    @Column(name="datePaidOn", nullable = false)
    private LocalDate datePaidOn;

    @Column(name="dueMonthDate", nullable = false)
    private LocalDate dueMonthDate;
    
    @Column(name="isFullyPaid", nullable = false)
    private Boolean isFullyPaid = false;



}
