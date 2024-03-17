package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.WorkDays;
import com.spring.Docdoc.entity.WorkTimes;
import com.spring.Docdoc.utilits.Enums.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkTimesRepository extends JpaRepository<WorkTimes,Long> {

    @Query( "SELECT i FROM WorkTimes i " +
            "LEFT JOIN BookAppointment b ON i.id = b.workTime.id " +
            "AND b.bookingDate = :date " +
            "AND (b.bookingState = :state1 OR b.bookingState = :state2) " +
            "WHERE i.workDays = :workDays " +
            "AND b.workTime IS NULL")
    List<WorkTimes> findByWorkDays(WorkDays workDays,
                                   String date,
                                   BookingState state1,
                                   BookingState state2);

    void deleteByWorkDays(WorkDays workDays);
}
