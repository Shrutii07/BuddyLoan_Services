package com.buddyloan.userauthservice.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@Table(name = "LoginDetails")
public class User {
    @Id
    @Column(name = "aadhaarId", length=12)
    private String aadhaarId;

    @Column(name="name", nullable = false)
    private String name;
    
    @Column(length=30, unique = true, name="email", nullable = false)
    private String email;

    @Column(length=20, name="password", nullable = false)
    private String password;

    @Column(name="feedback")
    private int feedback;
}
