package com.spring.Docdoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClinicDto {

    private Long id ;
    @NotBlank(message = "name is required")
    private String name ;

    private String image;

    @NotNull(message = "latitude is required")
    private double latitude;

    @NotNull(message = "longitude is required")
    private double longitude;

    @NotNull(message = "workDays is required")
    @Valid
    private List<WorkDayDto> workDays ;

    private DoctorDetailsDto doctorDetails;

}
