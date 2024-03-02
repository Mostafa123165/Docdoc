package com.spring.Docdoc.dto;

import lombok.Data;

@Data
public class ClinicResponse  {

    private Long id ;
    private String name ;
    private String image;
    private double latitude;
    private double longitude;

}
