package com.spring.Docdoc.dto;


import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.utilits.Enums.BookingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookAppointmentDto {

    @NotNull(message = "clinic_id is required")
    private Long clinicId;

    @NotNull(message = "bookingDate is required")
    private String bookingDate ;

    @NotNull(message = "workTimeId is required")
    private Long workTimeId ;

    @NotNull(message = "bookingType is required")
    private BookingType bookingType ;

    @NotBlank(message = "payment is required")
    private String payment ;




    // Regex patter for time in 12-hour format with AM | PM
    private static final String TIME_PATTERN = "^(1[0-2]|0[1-9]):[0-5][0-9] (AM|PM)$" ;
    private static final String DATE_PATTERN = "^([2-9][0-9][0-9][0-9])-(1[0-2]|0[1-9])-(1[0-9]|0[1-9]|2[0-9]|3[0-1])$" ;


    public void setBookingDate(String bookingDate) {
        validateDateFormat(bookingDate) ;
        this.bookingDate = bookingDate;
    }

    public static void validateDateFormat(String date) {
       if(!date.matches(DATE_PATTERN)) {
           throw new CustomException("Invalid bookingDate format. bookingDate should be in yyyy-MM-dd format.");
       }
    }


}

