package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.WorkTimesDto;
import com.spring.Docdoc.entity.WorkDays;
import com.spring.Docdoc.entity.WorkTimes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkTimesMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "workDays" ,  source = "workDays")
    @Mapping(target = "price" ,  source = "workTimesDto.price")
    WorkTimes mapToWorkTimes(WorkTimesDto workTimesDto , WorkDays workDays);
}
