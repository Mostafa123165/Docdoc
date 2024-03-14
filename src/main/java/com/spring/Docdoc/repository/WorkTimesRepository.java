package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.WorkDays;
import com.spring.Docdoc.entity.WorkTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkTimesRepository extends JpaRepository<WorkTimes,Long> {

    @Query( "SELECT i FROM WorkTimes i " +
            "LEFT JOIN BookAppointment b ON i.id = b.workTime.id " +
            "AND b.bookingDate = :date " +
            "WHERE i.workDays = :workDays " +
            "AND b.workTime IS NULL")
    List<WorkTimes> findByWorkDays(WorkDays workDays ,String date);
}
