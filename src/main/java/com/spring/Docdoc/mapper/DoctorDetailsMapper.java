package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.DoctorDetailsDto;
import com.spring.Docdoc.dto.UserDto;
import com.spring.Docdoc.entity.DoctorDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorDetailsMapper {


    @Mapping(target = "id" , source = "doctorDetails.id")
    @Mapping(target = "user" , source = "userDto")
    @Mapping(target = "speciality.name" , source = "doctorDetails.speciality.name")
    @Mapping(target = "speciality.image" , source = "doctorDetails.speciality.image")
    DoctorDetailsDto mapToDoctorDetailsDto(DoctorDetails doctorDetails , UserDto userDto) ;

}
