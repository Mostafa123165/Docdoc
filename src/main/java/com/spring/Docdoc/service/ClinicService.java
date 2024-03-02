package com.spring.Docdoc.service;

import com.spring.Docdoc.controller.ClinicController;
import com.spring.Docdoc.dto.ClinicDto;
import com.spring.Docdoc.dto.WorkDayDto;
import com.spring.Docdoc.dto.WorkTimesDto;
import com.spring.Docdoc.entity.*;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.mapper.ClinicMapper;
import com.spring.Docdoc.mapper.WorkDayMapper;
import com.spring.Docdoc.mapper.WorkTimesMapper;
import com.spring.Docdoc.repository.ClinicRepository;
import com.spring.Docdoc.repository.WorkDaysRepository;
import com.spring.Docdoc.repository.WorkTimesRepository;
import com.spring.Docdoc.utilits.comprator.TimeSortComparator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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


   public List<Clinic> findNearby(double latitude , double longitude , int page , int pageSize) {
       Pageable pageable = PageRequest.of(page, pageSize);

       return clinicRepository.findNearby(pageable,latitude,longitude);
   }


    public List<Clinic> findClinicsByDoctorDetails(DoctorDetails doctorDetails , int page , int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize) ;
        return  clinicRepository.findByDoctorDetails(doctorDetails , pageable);
    }

    public Clinic findById(Long clinicId) {
        Optional<Clinic> clinic = clinicRepository.findById(clinicId);
        return clinic.orElse(null) ;
    }

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
}
