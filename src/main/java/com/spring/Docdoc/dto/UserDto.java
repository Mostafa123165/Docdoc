package com.spring.Docdoc.dto;

import com.spring.Docdoc.utilits.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = "firstName is required")
    private String firstName ;

    @NotBlank(message = "lastName is required")
    private String lastName ;

    private String email ;

    @NotBlank(message = "phone is required")
    private String phone ;

    private String image;

    private Role role ;

}
