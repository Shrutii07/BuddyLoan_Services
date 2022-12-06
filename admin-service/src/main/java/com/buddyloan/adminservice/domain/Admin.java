package com.buddyloan.adminservice.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Entity
@Table(name = "AdminDetails")
public class Admin {
    @Id
    @Column(name = "employeeId", length=7)
    private String employeeId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(length=30, unique = true, name="email", nullable = false)
    private String email;

    @Column(length=20, name="password", nullable = false)
    private String password;

    @Column(name="image", nullable = false)
    private String image;
    
}
