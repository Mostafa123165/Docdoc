package com.spring.Docdoc.dto;


import com.spring.Docdoc.utilits.Enums.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableTimesDto {

    private Long clinicId ;
    private Day day ;
    private List<WorkTimesDto> availableTime ;
}
