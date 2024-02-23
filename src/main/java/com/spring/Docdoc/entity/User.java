package com.spring.Docdoc.entity;


import com.spring.Docdoc.utilits.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "first_name")
    private String firstName ;

    @Column(name = "last_name")
    private String lastName ;

    @Column(name = "password")
    private String password ;

    @Column(name = "email")
    private String email ;

    @Column(name = "phone")
    private String phone ;

    @Column(name = "image")
    private String image;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role ;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "created_at")
    private Instant CreatedAt ;

}
