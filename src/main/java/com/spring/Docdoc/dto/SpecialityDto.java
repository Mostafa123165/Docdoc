package com.spring.Docdoc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecialityDto {

    @NotBlank(message = "name is required")
    private String name ;

    private String image ;
}
