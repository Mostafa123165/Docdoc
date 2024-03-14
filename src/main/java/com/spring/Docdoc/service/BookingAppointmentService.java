package com.spring.Docdoc.service;

import com.spring.Docdoc.dto.*;
import com.spring.Docdoc.entity.*;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.mapper.*;
import com.spring.Docdoc.repository.BookAppointmentRepository;
import com.spring.Docdoc.repository.WorkDaysRepository;
import com.spring.Docdoc.repository.WorkTimesRepository;
import com.spring.Docdoc.utilits.Enums.BookingState;
import com.spring.Docdoc.utilits.Enums.Day;
import com.spring.Docdoc.utilits.Enums.Role;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingAppointmentService {

    final private BookAppointmentRepository bookAppointmentRepository;
    final private AuthService authService;
    final private BookAppointmentMapper bookAppointmentMapper;
    private final WorkDaysRepository workDaysRepository;
    private final WorkTimesRepository workTimesRepository;
    final private ReservationMapper reservationMapper;
    final private DoctorDetailsMapper doctorDetailsMapper;
    final private UserMapper userMapper;
    final private ClinicService clinicService;
    final private MyAppointmentMapper myAppointmentMapper;
    final private SpecialityMapper specialityMapper;

    @Transactional
    public ReservationDto addNewBookAppointment(BookAppointmentDto bookAppointmentDto) {

        User patient = authService.getCurrentUser();
        Clinic clinic = clinicService.findById(bookAppointmentDto.getClinicId());
        Optional<WorkTimes> workTimeOptional = workTimesRepository.findById(bookAppointmentDto.getWorkTimeId());

        if (workTimeOptional.isEmpty()) {
            throw new NotFoundException("Not found workTime with id: " + bookAppointmentDto.getWorkTimeId());
        }

        WorkTimes workTime = workTimeOptional.get();
        WorkDays workDay = workTime.getWorkDays();

        validateBookAppointmentRequest(patient, clinic, workDay, bookAppointmentDto);

        BookAppointment bookAppointment = bookAppointmentMapper.MapToBookAppointMent(
                bookAppointmentDto,
                patient,
                clinic.getDoctorDetails().getUser(),
                clinic,
                workTime,
                workDay
        );

        checkFoundBookAppointmentDto(workTime, bookAppointmentDto.getBookingDate());

        try {
            bookAppointmentRepository.save(bookAppointment);
        } catch (Exception e) {
            throw new CustomException("Failed to save book appointment." + e.getMessage());
        }

        DoctorDetails doctorDetails = clinic.getDoctorDetails();
        Hibernate.initialize(doctorDetails.getSpeciality());
        UserDto userDto = userMapper.mapToUserDto(doctorDetails.getUser());

        return reservationMapper
                .MapToReservationDto(
                        bookAppointment
                        , doctorDetailsMapper.mapToDoctorDetailsDto(doctorDetails, userDto)
                );
    }


    private void checkFoundBookAppointmentDto(WorkTimes workTimes, String bookingDate) {
        Optional<BookAppointment> bookAppointment = bookAppointmentRepository
                .findByBookingDateAndWorkTime(bookingDate, workTimes);
        if (bookAppointment.isPresent()) {
            throw new CustomException("The time is already reserved.");
        }

    }

    private void validateBookAppointmentRequest(User patient,
                                                Clinic clinic,
                                                WorkDays workDay,
                                                BookAppointmentDto bookAppointmentDto) {

        if (patient.getRole() == Role.DOCTOR) {
            throw new CustomException("Invalid appointment request. Only patients can make reservations.");
        }

        if (clinic == null) {
            throw new NotFoundException("Not found clinic with ID: " + clinic.getId());
        }

        if (!Objects.equals(workDay.getClinic().getId(), bookAppointmentDto.getClinicId())) {
            throw new CustomException("The clinic ID associated with the work time does not match: " + bookAppointmentDto.getClinicId());
        }

        if (!Objects.equals(workDay.getDay(), getDayByDate(bookAppointmentDto.getBookingDate()))) {
            throw new CustomException("The day associated with the work time does not match the day of the booking date: " + bookAppointmentDto.getBookingDate());
        }

        LocalDate date = LocalDate.parse(bookAppointmentDto.getBookingDate());
        LocalDate currentDate = LocalDate.now();
        long days = ChronoUnit.DAYS.between(currentDate, date);

        if (days > 30) {
            throw new CustomException("Reservations must be made within 30 days from today. Current difference: " + days + " days.");
        }

    }

    @Transactional
    public List<WorkTimes> findAvailableTime(Long clinicId, String date) {
        validateAvailableTimeRequest(date);

        Day day = getDayByDate(date);

        Clinic clinic = clinicService.findById(clinicId);

        if (clinic == null) {
            throw new NotFoundException("Clinic not found with ID:" + clinicId);
        }

        // Find work days for the clinic on the specified day
        Optional<WorkDays> workDaysOptional = workDaysRepository.findByClinicAndDay(clinic, day);
        if (workDaysOptional.isEmpty()) {
            // No work days found for the clinic on the specified day
            return Collections.emptyList();
        }

        return workTimesRepository.findByWorkDays(workDaysOptional.get(), date);

    }

    private void validateAvailableTimeRequest(String date) {

        BookAppointmentDto.validateDateFormat(date);

    }

    public static Day getDayByDate(String Date) {

        LocalDate date = LocalDate.parse(Date);
        DayOfWeek day = date.getDayOfWeek();

        return switch (day) {
            case FRIDAY -> Day.Friday;
            case WEDNESDAY -> Day.Wednesday;
            case MONDAY -> Day.Monday;
            case SATURDAY -> Day.Saturday;
            case SUNDAY -> Day.Sunday;
            case THURSDAY -> Day.Thursday;
            default -> Day.Tuesday;
        };
    }

    @Transactional
    public List<MyAppointmentDto> findMyAppointmentForUser(
            BookingState bookingState,
            int page,
            int pageSize) {
        User user = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<BookAppointment> bookAppointments = bookAppointmentRepository
                .findByUserAndBookingState(
                        user,
                        bookingState,
                        pageable);


        return bookAppointments
                .stream()
                .map(item -> {
                    DoctorDetails doctorDetails = item.getClinic().getDoctorDetails();
                    SpecialityDto specialityDto = specialityMapper
                            .mapToSpecialityDto(doctorDetails.getSpeciality());

                    User user1 = item.getDoctor();

                    return myAppointmentMapper.mapTpMyAppointmentDto(
                            item,
                            specialityDto,
                            user1
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MyAppointmentDto> findMyAppointmentForDoctor(
            BookingState bookingState,
            int page,
            int pageSize) {
        User user = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, pageSize);

        List<BookAppointment> bookAppointments = bookAppointmentRepository
                .findByDoctorAndBookingState(
                        user,
                        bookingState,
                        pageable);


        return bookAppointments
                .stream()
                .map(item -> {
                    DoctorDetails doctorDetails = item.getClinic().getDoctorDetails();
                    SpecialityDto specialityDto = specialityMapper
                            .mapToSpecialityDto(doctorDetails.getSpeciality());

                    User user1 = item.getUser();

                    return myAppointmentMapper.mapTpMyAppointmentDto(
                            item,
                            specialityDto,
                            user1
                    );
                })
                .collect(Collectors.toList());
    }

}
