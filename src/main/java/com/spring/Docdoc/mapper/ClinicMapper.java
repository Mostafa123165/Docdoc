package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.ClinicDto;
import com.spring.Docdoc.entity.Clinic;
import com.spring.Docdoc.entity.DoctorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(target = "doctorDetails" , source = "doctorDetails")
    @Mapping(target = "id" , ignore = true)
    Clinic mapToClinic(ClinicDto clinicDto , DoctorDetails doctorDetails) ;

    @Mapping(target = "workDays" , ignore = true)
    ClinicDto mapToClinicDto(Clinic clinic);
}
