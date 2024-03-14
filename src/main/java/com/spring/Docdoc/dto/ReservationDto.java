package com.spring.Docdoc.dto;

import com.spring.Docdoc.utilits.Enums.BookingType;
import com.spring.Docdoc.utilits.Enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {

    private Long id ;
    private String bookingDate ;
    private BookingType bookingType;
    private Day day;
    private String start ;
    private String end ;
    private double subtotal ;
    private double tax ;
    private double paymentTotal ;
    private DoctorDetailsDto doctorDetailsDto ;

}
