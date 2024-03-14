package com.spring.Docdoc.entity;

import com.spring.Docdoc.utilits.Enums.BookingState;
import com.spring.Docdoc.utilits.Enums.BookingType;
import com.spring.Docdoc.utilits.Enums.Day;
import jakarta.persistence.*;
import lombok.Data;



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
    @JoinColumn(name = "doctor_id")
    private User doctor ;

    @OneToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @Column(name = "booking_date")
    private String bookingDate ;

    @OneToOne
    @JoinColumn(name = "work_time_id")
    private WorkTimes workTime ;

    @OneToOne
    @JoinColumn(name = "work_day_id")
    private WorkDays workDay ;


    @Column(name = "booking_state")
    @Enumerated(EnumType.STRING)
    private BookingState bookingState ;

    @Column(name = "booking_type")
    @Enumerated(EnumType.STRING)
    private BookingType bookingType ;

    @Column(name = "payment")
    private String payment ;

    @Column(name = "price")
    private double price;
}

