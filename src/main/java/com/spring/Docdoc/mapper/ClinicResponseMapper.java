package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.ClinicResponse;
import com.spring.Docdoc.entity.Clinic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicResponseMapper {

    ClinicResponse mapToClinicResponse(Clinic clinic);
}
