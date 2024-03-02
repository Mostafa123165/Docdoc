package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.WorkDayDto;
import com.spring.Docdoc.entity.Clinic;
import com.spring.Docdoc.entity.WorkDays;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkDayMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "day" , source = "workDayDto.day")
    @Mapping(target = "clinic" , source = "clinic")
    WorkDays mapToWorkDay(WorkDayDto workDayDto , Clinic clinic);
}
