package com.spring.Docdoc.dto;


import lombok.Data;

@Data
public class MyAppointmentDto {

    private String image ;
    private String firstName;
    private String lastName;
    private SpecialityDto specialityDto;
    private String bookingDate;
    private String startTime ;
    private String endTime ;
}
