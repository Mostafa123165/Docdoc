package com.spring.Docdoc.service;

import com.spring.Docdoc.dto.*;
import com.spring.Docdoc.entity.*;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.mapper.*;
import com.spring.Docdoc.repository.BookAppointmentRepository;
import com.spring.Docdoc.repository.ClinicRepository;
import com.spring.Docdoc.repository.WorkDaysRepository;
import com.spring.Docdoc.repository.WorkTimesRepository;
import com.spring.Docdoc.utilits.Enums.Day;
import com.spring.Docdoc.utilits.Enums.Role;
import com.spring.Docdoc.utilits.comprator.TimeSortComparator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository ;
    private final WorkDaysRepository workDaysRepository ;
    private final WorkTimesRepository workTimesRepository;
    private final ClinicMapper clinicMapper ;
    private final WorkDayMapper workDayMapper;
    private final WorkTimesMapper workTimesMapper;
    private final ImageService imageService;
    private final AuthService authService;
    private final DoctorService doctorService;



    @Transactional
    public Long save(ClinicDto clinicDto , DoctorDetails doctorDetails) {

        checkTimesInWorkDays(clinicDto.getWorkDays()) ;

        Clinic clinic = clinicMapper.mapToClinic(clinicDto , doctorDetails);

        clinicRepository.save(clinic) ;

        for(WorkDayDto workDayDto : clinicDto.getWorkDays()) {

            WorkDays workDays = workDayMapper.mapToWorkDay(workDayDto , clinic);
            workDaysRepository.save(workDays);

            for(WorkTimesDto workTimesDto : workDayDto.getWorkTimes()) {
                WorkTimes workTimes = workTimesMapper.mapToWorkTimes(workTimesDto , workDays);
                workTimesRepository.save(workTimes);
            }
        }

        return clinic.getId() ;

    }

    private void checkTimesInWorkDays(List<WorkDayDto> workDaysDto) {

        boolean isValidate = true ;
        for(WorkDayDto workDays : workDaysDto) {

            workDays.getWorkTimes().sort(new TimeSortComparator());
            isValidate = workDays.isValidateDay();
            if(!isValidate) break;
        }


        if(!isValidate) throw new CustomException("Invalid time schedule detected.");
    }


    @Transactional
   public List<Clinic> findNearby(double latitude , double longitude , int page , int pageSize) {
       Pageable pageable = PageRequest.of(page, pageSize);

       return clinicRepository.findNearby(pageable,latitude,longitude);
   }


   @Transactional
    public List<Clinic> findClinicsByDoctorDetails(DoctorDetails doctorDetails , int page , int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize) ;
        return  clinicRepository.findByDoctorDetails(doctorDetails , pageable);
    }

    public Clinic findById(Long clinicId) {
        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
        return clinic.orElse(null) ;
    }

    @Transactional
    public void uploadImage(Long clinicId ,
                            MultipartFile multipartFile ,
                            String imagePath) throws IOException {
        Clinic clinic = findById(clinicId) ;

        if(clinic == null)
            throw new NotFoundException("Clinic not found with id - "+ clinicId + ".") ;

        if(multipartFile == null)
            throw new CustomException("MultipartFile cannot be null.");


        imageService.saveImage(multipartFile ,imagePath);

        clinic.setImage(imagePath);
        clinicRepository.save(clinic);
    }

    @Transactional
    public void delete(Long id) {
        User user = authService.getCurrentUser();
        DoctorDetails doctorDetails = doctorService.findByUser(user);

        Optional<Clinic> clinicOptional = clinicRepository.findByIdAndDoctorDetails(
                id,
                doctorDetails
        );

        if(clinicOptional.isPresent()) {
            Clinic clinic = clinicOptional.get() ;
            List<WorkDays> workDays = workDaysRepository.findByClinic(clinic);
            for(WorkDays workDay : workDays) {
                workTimesRepository.deleteByWorkDays(workDay);
                workDaysRepository.delete(workDay);
            }
            clinicRepository.delete(clinicOptional.get());
        }
        else
            throw new NotFoundException("The doctor is not associated with this clinic ID:" + id);
    }
}
