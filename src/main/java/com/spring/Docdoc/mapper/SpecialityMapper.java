package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.SpecialityDto;
import com.spring.Docdoc.entity.Speciality;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    SpecialityDto mapToSpecialityDto(Speciality speciality);
}
