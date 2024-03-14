package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.AvailableTimesDto;
import com.spring.Docdoc.dto.BookAppointmentDto;

import com.spring.Docdoc.dto.MyAppointmentDto;
import com.spring.Docdoc.dto.ReservationDto;
import com.spring.Docdoc.entity.WorkTimes;
import com.spring.Docdoc.mapper.WorkTimesMapper;
import com.spring.Docdoc.service.BookingAppointmentService;
import com.spring.Docdoc.utilits.Enums.BookingState;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BookingAppointmentController {

    private final BookingAppointmentService bookingAppointmentService;
    private final WorkTimesMapper workTimesMapper;

    @PostMapping("/booking-appointment")
    public ReservationDto save(@Valid @RequestBody BookAppointmentDto bookAppointmentDto) {

        return bookingAppointmentService.addNewBookAppointment(bookAppointmentDto);
    }

    @GetMapping("/booking-appointment/availableTime")
    public AvailableTimesDto findAvailableTime(
             @RequestParam Long clinicId
            ,@RequestParam String date) {

        List<WorkTimes> workTimes =  bookingAppointmentService.findAvailableTime(clinicId  , date);

        return AvailableTimesDto.builder()
                .availableTime(workTimes.stream().
                        map(workTimesMapper::mapToWorkTimesDto).collect(Collectors.toList()))
                .day(BookingAppointmentService.getDayByDate(date))
                .clinicId(clinicId).build();
    }

    @GetMapping("/my-appointment/patient")
    public List<MyAppointmentDto> getMyAppointmentForPatient(@RequestParam BookingState BookingState,
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

}

