package com.spring.Docdoc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank(message = "email is required")
    private String email ;

    @NotBlank(message = "password is required")
    public String password ;
}
