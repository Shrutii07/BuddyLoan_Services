package com.buddyloan.adminservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminDto {
    
    @Pattern(regexp="^[R][B][S][0-9]{4}" , message = "Employee Id must start with RBS followed by 4 digits.")
    private  String employeeId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$", message = "Invalid password. Password must contain 1 letter, 1 number, 1 special character and needs to be of 8-20 characters.")
    private String password;

    @NotBlank(message = "Image link is mandatory")
    private String image;
}
