package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorDetailsRepository extends JpaRepository<DoctorDetails,Long> {
}
