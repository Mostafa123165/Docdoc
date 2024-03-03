/*
package com.spring.Docdoc.entity;

import com.spring.Docdoc.utilits.Enums.BookingState;
import com.spring.Docdoc.utilits.Enums.BookingType;
import com.spring.Docdoc.utilits.Enums.Day;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "book_appointment")
public class BookAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @OneToOne
    @JoinColumn(name = "paint_id")
    private User user ;

    @OneToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private DoctorDetails doctorDetails ;

    @Column(name = "booking_date")
    private Instant bookingDate ;

    @Column(name = "start_time")
    private Instant startTime ;

    @Column(name = "end_time")
    private Instant endTime ;

    @Column(name = "day")
    private Day day ;

    @Column(name = "booking_state")
    private BookingState bookingState ;

    @Column(name = "booking_type")
    private BookingType bookingType ;

    @Column(name = "payment")
    private String payment ;

    @Column(name = "price")
    private double price;
}
*/
