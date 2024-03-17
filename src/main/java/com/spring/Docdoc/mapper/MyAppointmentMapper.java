package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.MyAppointmentDto;
import com.spring.Docdoc.dto.SpecialityDto;
import com.spring.Docdoc.entity.BookAppointment;
import com.spring.Docdoc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MyAppointmentMapper {


    @Mapping(target = "id",          source = "bookAppointment.id")
    @Mapping(target = "image",       source = "user.image")
    @Mapping(target = "firstName",   source = "user.firstName")
    @Mapping(target = "lastName",    source = "user.lastName")
    @Mapping(target = "bookingDate", source = "bookAppointment.bookingDate")
    @Mapping(target = "startTime",   source = "bookAppointment.workTime.start")
    @Mapping(target = "endTime",     source = "bookAppointment.workTime.end")
    MyAppointmentDto mapTpMyAppointmentDto(BookAppointment bookAppointment,
                                           SpecialityDto specialityDto,
                                           User user);

}
