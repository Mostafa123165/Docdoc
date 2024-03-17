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
import com.spring.Docdoc.utilits.Enums.BookingType;
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
    public ReservationDto addNewBookAppointment(BookingAppointmentDto bookingAppointmentDto) {

        User patient = authService.getCurrentUser();
        Clinic clinic = clinicService.findById(bookingAppointmentDto.getClinicId());
        Optional<WorkTimes> workTimeOptional = workTimesRepository.findById(bookingAppointmentDto.getWorkTimeId());

        if (workTimeOptional.isEmpty()) {
            throw new NotFoundException("Not found workTime with ID: " + bookingAppointmentDto.getWorkTimeId());
        }

        WorkTimes workTime = workTimeOptional.get();
        WorkDays workDay = workTime.getWorkDays();

        validateBookAppointmentRequest(patient, clinic, workDay, bookingAppointmentDto);

        BookAppointment bookAppointment = bookAppointmentMapper.MapToBookAppointMent(
                bookingAppointmentDto,
                patient,
                clinic.getDoctorDetails().getUser(),
                clinic,
                workTime,
                workDay
        );

        checkFoundBookAppointmentDto(workTime, bookingAppointmentDto.getBookingDate());

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

        List<BookingState> bookingStates = Arrays.asList(BookingState.UPCOMING, BookingState.COMPLETED);

        Optional<BookAppointment> bookAppointment = bookAppointmentRepository
                .findByBookingDateAndWorkTimeAndBookingStates(
                        bookingDate,
                        workTimes,
                        bookingStates);

        if (bookAppointment.isPresent()) {
            throw new CustomException("The time is already reserved.");
        }

    }

    private void validateBookAppointmentRequest(User patient,
                                                Clinic clinic,
                                                WorkDays workDay,
                                                BookingAppointmentDto bookingAppointmentDto) {

        if (patient.getRole() == Role.DOCTOR) {
            throw new CustomException("Invalid appointment request. Only patients can make reservations.");
        }

        if (clinic == null) {
            throw new NotFoundException("Not found clinic with ID: " + clinic.getId());
        }

        if (!Objects.equals(workDay.getClinic().getId(), bookingAppointmentDto.getClinicId())) {
            throw new CustomException("The clinic ID associated with the work time does not match: " + bookingAppointmentDto.getClinicId());
        }

        if (!Objects.equals(workDay.getDay(), getDayByDate(bookingAppointmentDto.getBookingDate()))) {
            throw new CustomException("The day associated with the work time does not match the day of the booking date: " + bookingAppointmentDto.getBookingDate());
        }

        LocalDate date = LocalDate.parse(bookingAppointmentDto.getBookingDate());
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

        return workTimesRepository.findByWorkDays(
                workDaysOptional.get(),
                date,
                BookingState.UPCOMING,
                BookingState.COMPLETED
        );

    }

    private void validateAvailableTimeRequest(String date) {

        BookingAppointmentDto.validateDateFormat(date);

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


    public BookAppointment findByIdAndPatient(Long id) {

        Optional<BookAppointment> bookAppointmentOptional = bookAppointmentRepository
                .findByIdAndUser(
                        id,
                        authService.getCurrentUser()
                );

        if(bookAppointmentOptional.isEmpty()) {
            bookAppointmentOptional = bookAppointmentRepository
                    .findByIdAndDoctor(
                            id,
                            authService.getCurrentUser()
                    );
        }

        return bookAppointmentOptional.orElse(null);
    }

    @Transactional
    public void update(BookAppointment bookAppointment) {

    }

    @Transactional
    public void delete(Long id) {
        BookAppointment bookAppointment = findByIdAndPatient(id);

        if(bookAppointment == null) {
            throw new NotFoundException("Invalid reservation or patient id");
        }

        bookAppointment.setBookingState(BookingState.CANCELLED);

        bookAppointmentRepository.save(bookAppointment);
    }
}
