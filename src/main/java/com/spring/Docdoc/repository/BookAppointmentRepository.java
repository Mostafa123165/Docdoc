package com.spring.Docdoc.repository;


import com.spring.Docdoc.entity.BookAppointment;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.entity.WorkTimes;
import com.spring.Docdoc.utilits.Enums.BookingState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookAppointmentRepository extends JpaRepository<BookAppointment,Long> {

    @Query("SELECT i FROM BookAppointment i " +
            "WHERE i.bookingDate = :bookingDate " +
            "AND i.workTime = :workTime " +
            "AND i.bookingState IN :bookingState")
    Optional<BookAppointment> findByBookingDateAndWorkTimeAndBookingStates(
            String bookingDate,
            WorkTimes workTime,
            List<BookingState> bookingState);

    List<BookAppointment> findByUserAndBookingState(User user ,
                                                    BookingState bookingState ,
                                                    Pageable pageable);

    List<BookAppointment> findByDoctorAndBookingState(User user ,
                                                    BookingState bookingState ,
                                                    Pageable pageable);

    Optional<BookAppointment> findByIdAndUser(Long id, User user);

    Optional<BookAppointment> findByIdAndDoctor(Long id, User doctor);
}

