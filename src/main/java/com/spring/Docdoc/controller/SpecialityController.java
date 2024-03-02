package com.spring.Docdoc.controller;

import com.spring.Docdoc.entity.Speciality;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.service.DoctorService;
import com.spring.Docdoc.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/speciality")
public class SpecialityController {

    private final DoctorService doctorService;
    private final ImageService  imageService;



    @PostMapping
    public ResponseMessage save(@RequestParam String specialityName ,
                                @RequestParam MultipartFile multipartFile) {
        try {

            Speciality speciality = doctorService.findBySpecialityName(specialityName) ;
            if(speciality != null) {
                throw new CustomException("Specialty name already exists.");
            }

            doctorService.saveSpeciality(specialityName , multipartFile );

            return ResponseMessage
                    .builder()
                    .status(HttpStatus.OK.value())
                    .message("Saved specialty successfully")
                    .build();

        } catch (IOException e) {

            throw new CustomException("Failed to save image: " + e.getMessage());
        }

    }


    @GetMapping
    public ResponseEntity<List<Speciality>> findAll() {

        return ResponseEntity.status(HttpStatus.OK)
                .body(doctorService.findAllSpeciality());
    }

    @DeleteMapping
    public ResponseMessage delete(@RequestParam Long id) {

        Speciality speciality = doctorService.findSpecialityById(id) ;
        imageService.deleteImageByName(speciality.getImage()) ;
        doctorService.deleteSpecialityById(id) ;

        return ResponseMessage
                .builder()
                .status(HttpStatus.OK.value())
                .message("Deleted speciality successfully")
                .build();
    }
}
