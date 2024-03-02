package com.spring.Docdoc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "otp_value")
    private String otpValue ;

    @Column(name = "created_at")
    private Instant createdAt ;

    @Column(name = "expired_at")
    private Instant expiredAt ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user ;

}
