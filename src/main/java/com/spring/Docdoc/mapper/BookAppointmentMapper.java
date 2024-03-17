package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.BookingAppointmentDto;
import com.spring.Docdoc.entity.*;
import com.spring.Docdoc.service.BookingAppointmentService;
import com.spring.Docdoc.utilits.Enums.BookingState;
import com.spring.Docdoc.utilits.Enums.Day;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookAppointmentMapper {


    @Mapping(target = "id" , source = "bookingAppointmentDto.id")
    @Mapping(target = "bookingState" ,expression = "java(getBookingState())")
    @Mapping(target = "bookingType" ,source = "bookingAppointmentDto.bookingType")
    @Mapping(target = "payment" ,source = "bookingAppointmentDto.payment")
    @Mapping(target = "price" ,source = "workTime.price")
    @Mapping(target = "workTime" ,source = "workTime")
    @Mapping(target = "workDay" ,source = "workDay")
    BookAppointment MapToBookAppointMent(
            BookingAppointmentDto bookingAppointmentDto,
            User user,
            User doctor,
            Clinic clinic,
            WorkTimes workTime,
            WorkDays workDay
    );


    default BookingState getBookingState() {
        return BookingState.UPCOMING ;
    }

    default Day getDay(BookingAppointmentDto bookingAppointmentDto) {

        return  BookingAppointmentService.getDayByDate(bookingAppointmentDto.getBookingDate()) ;

    }

}
