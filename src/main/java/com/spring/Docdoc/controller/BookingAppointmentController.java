package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.AvailableTimesDto;
import com.spring.Docdoc.dto.BookingAppointmentDto;

import com.spring.Docdoc.dto.MyAppointmentDto;
import com.spring.Docdoc.dto.ReservationDto;
import com.spring.Docdoc.entity.WorkTimes;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.mapper.WorkTimesMapper;
import com.spring.Docdoc.service.BookingAppointmentService;
import com.spring.Docdoc.utilits.Enums.BookingState;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api/booking-appointment")
public class BookingAppointmentController {

    private final BookingAppointmentService bookingAppointmentService;
    private final WorkTimesMapper workTimesMapper;

    @PostMapping
    public ReservationDto save(@Valid @RequestBody
                                   BookingAppointmentDto bookingAppointmentDto) {

        return bookingAppointmentService.addNewBookAppointment(bookingAppointmentDto);
    }

    @GetMapping("/availableTime")
    public AvailableTimesDto findAvailableTime(
             @RequestParam Long clinicId
            ,@RequestParam String date) {

        List<WorkTimes> workTimes =  bookingAppointmentService.findAvailableTime(clinicId, date);

        return AvailableTimesDto.builder()
                .availableTime(workTimes.stream().
                        map(workTimesMapper::mapToWorkTimesDto).collect(Collectors.toList()))
                .day(BookingAppointmentService.getDayByDate(date))
                .clinicId(clinicId).build();
    }

    @GetMapping("/my-appointment/patient")
    public List<MyAppointmentDto> getMyAppointmentForPatient(
            @RequestParam BookingState BookingState,
            @RequestParam int page) {

        return  bookingAppointmentService.findMyAppointmentForUser(
                BookingState,
                page,
                10);
    }

    @GetMapping("/my-appointment/doctor")
    public List<MyAppointmentDto> getMyAppointmentForDoctor(
            @RequestParam BookingState BookingState,
            @RequestParam int page) {
        return  bookingAppointmentService.findMyAppointmentForDoctor(
                BookingState,
                page,
                10);
    }

   /* @PutMapping
    public ResponseMessage updateBookingAppointment(@Valid @RequestBody
                                                        BookingAppointmentDto bookingAppointmentDto) {

        bookingAppointmentService.update(bookingAppointmentDto);

        return ResponseMessage
                .builder()
                .status(HttpStatus.OK.value())
                .message("Updated reservation successfully")
                .build();
    }*/

    @DeleteMapping
    public ResponseMessage cancelBookingAppointment(@RequestParam Long id) {

        bookingAppointmentService.delete(id);

        return ResponseMessage
                .builder()
                .status(HttpStatus.OK.value())
                .message("Canceled reservation successfully")
                .build();
    }
}

