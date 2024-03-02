package com.spring.Docdoc.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor")
@AllArgsConstructor
public class DoctorController {


    @PostMapping
    public void save() {
    }

}
