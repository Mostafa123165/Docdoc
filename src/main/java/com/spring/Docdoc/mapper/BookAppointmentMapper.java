package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.BookAppointmentDto;
import com.spring.Docdoc.entity.*;
import com.spring.Docdoc.service.BookingAppointmentService;
import com.spring.Docdoc.utilits.Enums.BookingState;
import com.spring.Docdoc.utilits.Enums.Day;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookAppointmentMapper {


    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "bookingState" ,expression = "java(getBookingState())")
    @Mapping(target = "bookingType" ,source = "bookAppointmentDto.bookingType")
    @Mapping(target = "payment" ,source = "bookAppointmentDto.payment")
    @Mapping(target = "price" ,source = "workTime.price")
    @Mapping(target = "workTime" ,source = "workTime")
    @Mapping(target = "workDay" ,source = "workDay")
    BookAppointment MapToBookAppointMent(
            BookAppointmentDto bookAppointmentDto,
            User user,
            User doctor,
            Clinic clinic,
            WorkTimes workTime,
            WorkDays workDay
    );


    default BookingState getBookingState() {
        return BookingState.UPCOMING ;
    }

    default Day getDay(BookAppointmentDto bookAppointmentDto) {

        return  BookingAppointmentService.getDayByDate(bookAppointmentDto.getBookingDate()) ;

    }

}
