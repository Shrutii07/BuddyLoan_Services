package com.buddyloan.userauthservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    @Pattern(regexp = "^[2-9]{1}[0-9]{11}$", message = "Invalid aadhaar number.")
    private  String aadhaarId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$", message = "Invalid password. Password must contain 1 letter, 1 number, 1 special character and needs to be of 8-20 characters.")
    private String password;

    private int feedback =8;
    
}
