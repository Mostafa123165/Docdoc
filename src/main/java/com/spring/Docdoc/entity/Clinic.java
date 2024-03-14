package com.spring.Docdoc.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "image")
    private String image;

    @Column(name = "latitude")
    private double latitude ;

    @Column(name = "longitude")
    private double longitude ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorDetails doctorDetails;

}
