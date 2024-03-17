package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.Clinic;
import com.spring.Docdoc.entity.DoctorDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic,Long> {

    @Query("SELECT c FROM Clinic c " +
            "JOIN FETCH c.doctorDetails jo " +
            "JOIN FETCH jo.speciality sp " +
            "JOIN FETCH jo.user u " +
            "WHERE " +
            "SQRT(POWER(c.latitude - :latitude, 2) + POWER(c.longitude - :longitude, 2)) * 111.111 <= 1.0")
    List<Clinic> findNearby(Pageable pageable, double latitude, double longitude);

    List<Clinic> findByDoctorDetails(DoctorDetails doctorDetails,Pageable pageable) ;

    Optional<Clinic> findByIdAndDoctorDetails(Long id , DoctorDetails doctorDetails);

}
