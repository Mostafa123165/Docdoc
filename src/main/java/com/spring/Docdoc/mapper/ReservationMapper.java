package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.DoctorDetailsDto;
import com.spring.Docdoc.dto.ReservationDto;
import com.spring.Docdoc.entity.BookAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "id" , source = "bookAppointment.id")
    @Mapping(target = "bookingDate" , source = "bookAppointment.bookingDate")
    @Mapping(target = "bookingType" , source = "bookAppointment.bookingType")
    @Mapping(target = "day" , source = "bookAppointment.workDay.day")
    @Mapping(target = "start" , source = "bookAppointment.workTime.start")
    @Mapping(target = "end" , source = "bookAppointment.workTime.end")
    @Mapping(target = "doctorDetailsDto" , source = "doctorDetailsDto")
    @Mapping(target = "subtotal" , expression = "java(bookAppointment.getPrice())")
    @Mapping(target = "tax"  , expression = "java(getTax(bookAppointment.getPrice()))" )
    @Mapping(target = "paymentTotal",expression = "java(getPaymentTotal(bookAppointment.getPrice()))" )
    ReservationDto MapToReservationDto(BookAppointment bookAppointment,
                                       DoctorDetailsDto doctorDetailsDto);

    default double getTax(double price) {
        return price * 0.1 ;
    }

    default double getPaymentTotal(double price) {
        double totalAmount = price + getTax(price);
        return roundToTwoDecimalPlaces(totalAmount) ;
    }

    default double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
