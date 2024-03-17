package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.Clinic;
import com.spring.Docdoc.entity.WorkDays;
import com.spring.Docdoc.utilits.Enums.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkDaysRepository extends JpaRepository<WorkDays,Long> {


    Optional<WorkDays> findByClinicAndDay(Clinic clinic , Day day) ;
    List<WorkDays> findByClinic(Clinic clinic) ;

}
