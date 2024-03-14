package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.ClinicDto;
import com.spring.Docdoc.dto.ClinicResponse;
import com.spring.Docdoc.entity.Clinic;
import com.spring.Docdoc.entity.DoctorDetails;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.mapper.ClinicMapper;
import com.spring.Docdoc.mapper.ClinicResponseMapper;
import com.spring.Docdoc.service.*;

import com.spring.Docdoc.utilits.Enums.Day;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clinic")
@AllArgsConstructor
public class ClinicController {

    final private AuthService authService;
    final private ClinicService clinicService ;
    final private DoctorService doctorService;
    final private ClinicMapper clinicMapper;
    final private ClinicResponseMapper clinicResponseMapper;
    final private ImageService imageService;
    final private UserService userService;

    @PostMapping
    public ResponseMessage save(@Valid @RequestBody ClinicDto clinicDto) {

        User user = authService.getCurrentUser();

        DoctorDetails doctorDetails =  doctorService.findByUser(user);
        Long clinicId = clinicService.save(clinicDto , doctorDetails);

        return ResponseMessage
                .builder()
                .message("Saved clinic successfully - " + clinicId)
                .status(HttpStatus.OK.value())
                .build();
    }

    @PostMapping("/image")
    public ResponseMessage uploadClinicImage(
                       @RequestParam MultipartFile multipartFile ,
                       @RequestParam Long clinicId) throws IOException {

        String imagePath = clinicId
                + "-" + "Clinic"
                + imageService.getExtensionImage(multipartFile.getOriginalFilename()) ;

        clinicService.uploadImage(clinicId,multipartFile ,imagePath);

        return ResponseMessage.builder().status(HttpStatus.OK.value())
                .message("Uploaded image successfully").build();
    }

    @GetMapping("/findNearby")
    public List<ClinicDto> findNearby(@RequestParam double latitude ,
                                      @RequestParam double longitude ,
                                      @RequestParam int page) {

        List<Clinic> clinics = clinicService.findNearby(
                latitude
                ,longitude,page
                ,10);

        return clinics
                .stream()
                .map(clinicMapper::mapToClinicDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/doctorClinics")
    public List<ClinicResponse> findDoctorClinics(@RequestParam int page
                                                  , @RequestParam Long id) {
        User user = userService.findById(id) ;

        List<Clinic> clinics =  clinicService.findClinicsByDoctorDetails(
                doctorService.findByUser(user)
                ,page
                ,10);

        return clinics
                .stream()
                .map(clinicResponseMapper::mapToClinicResponse)
                .collect(Collectors.toList()) ;
    }



 }
