package com.spring.Docdoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
public class WorkTimesDto {

    @NotBlank(message = "start is required")
    private String start ;

    @NotBlank(message = "end is required")
    private String end ;

    @Min(value = 1 , message = "price is required")
    @NotNull(message = "price is required")
    private double price ;


    @JsonIgnore
    String regex = "^(1[0-2]|0[1-9]):[0-5][0-9] (AM|PM)$";

    @JsonIgnore
    public boolean isValidate() {
        boolean validateTime = true ;

        validateTime = getStart().matches(regex);
        validateTime &= getEnd().matches(regex) ;

        LocalTime startTime = localTime(getStart()) ;
        LocalTime endTime = localTime(getEnd()) ;

        validateTime &= startTime.isBefore(endTime) ;

        return validateTime ;
    }


    public boolean validateTimeIntersection(String end ,String start) {
        LocalTime startTime = localTime(start) ;
        LocalTime endTime = localTime(end) ;
        return startTime.isAfter(endTime) ;
    }

    @JsonIgnore
    private LocalTime localTime(String time) {

        return LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));
    }

}
