package com.spring.Docdoc.dto;

import com.spring.Docdoc.entity.Speciality;
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

    private Speciality speciality ;

    private UserDto user ;
}
