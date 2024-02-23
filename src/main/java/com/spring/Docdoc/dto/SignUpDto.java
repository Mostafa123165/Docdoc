package com.spring.Docdoc.dto;

import com.spring.Docdoc.utilits.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = "firstName is required")
    private String firstName ;

    @NotBlank(message = "lastName is required")
    private String lastName ;

    @NotBlank(message = "password is required")
    private String password ;

    @Email(message = "email is not valid")
    @NotNull(message = "email is required")
    private String email ;

    @NotBlank(message = "phone is required")
    private String phone ;

    @NotNull(message = "role is required")
    private Role role ;
}
