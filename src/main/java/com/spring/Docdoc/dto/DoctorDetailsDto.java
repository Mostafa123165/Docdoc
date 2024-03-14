package com.spring.Docdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailsDto {

    private int id ;

    private double rate ;

    private Long numOfReviews ;

    private String aboutMe ;

    private SpecialityDto speciality ;

    private UserDto user ;
}
