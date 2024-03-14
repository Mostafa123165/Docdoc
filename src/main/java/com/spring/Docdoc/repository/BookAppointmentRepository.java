package com.spring.Docdoc.repository;


import com.spring.Docdoc.entity.BookAppointment;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.entity.WorkTimes;
import com.spring.Docdoc.utilits.Enums.BookingState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BookAppointmentRepository extends JpaRepository<BookAppointment,Long> {
    Optional<BookAppointment> findByBookingDateAndWorkTime(String bookingDate ,
                                                                     WorkTimes workTime);

    List<BookAppointment> findByUserAndBookingState(User user ,
                                                    BookingState bookingState ,
                                                    Pageable pageable);

    List<BookAppointment> findByDoctorAndBookingState(User user ,
                                                    BookingState bookingState ,
                                                    Pageable pageable);
}

