package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.DoctorDetails;
import com.spring.Docdoc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<DoctorDetails , Long> {
     DoctorDetails findByUser(User user) ;
}
