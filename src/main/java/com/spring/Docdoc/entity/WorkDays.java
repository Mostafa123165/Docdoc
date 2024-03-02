package com.spring.Docdoc.entity;

import com.spring.Docdoc.utilits.Enums.Day;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;

@Entity
@Data
@Table(name = "work_days")
public class WorkDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id ;

    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private Day day ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic ;

}
