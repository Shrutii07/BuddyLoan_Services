package com.buddyloan.userbankservice.domain;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@Table(name="bankDetails")
public class Bank {
    @Id
    @Column(name="accountNum", length = 6)
    private int accountNum;

    @Column(name="bankName", nullable = false)
    private String bankName;

    @Column(name="bankBalance",nullable = false)
    private double bankBalance;

    @Column(name="aadhaarId",nullable = false)
    private String aadhaarId;

    @Column(name="pin",nullable = false, length = 4)
    private int pin;

}
