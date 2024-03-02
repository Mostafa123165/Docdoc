package com.spring.Docdoc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Docdoc.utilits.Enums.Day;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class WorkDayDto {

    @NotNull(message = "day is required")
    private Day day ;

    @NotNull(message = "workTimes is required")
    @NotEmpty(message = "workTimes is required")
    @Valid
    private List<WorkTimesDto> workTimes;


    @JsonIgnore
    public boolean isValidateDay() {

        for(WorkTimesDto times : getWorkTimes()) {

            if(!times.isValidate())  return false ;
        }

        // validate time intersection, after sort workTime in day
        for(int time = 1; time< getWorkTimes().size() ; time++ ) {
            boolean res = getWorkTimes().get(time).validateTimeIntersection(
                    getWorkTimes().get(time-1).getEnd() ,
                    getWorkTimes().get(time).getStart()) ;
            if(!res) return false ;
        }

        return true;
    }



}
