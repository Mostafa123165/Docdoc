package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.Otp;
import com.spring.Docdoc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findByUser(User user);
}
