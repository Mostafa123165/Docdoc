package com.spring.Docdoc.service;

import com.spring.Docdoc.entity.DoctorDetails;
import com.spring.Docdoc.entity.Speciality;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.repository.DoctorRepository;
import com.spring.Docdoc.repository.SpecialityRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class DoctorService {

    final private DoctorRepository doctorRepository ;
    final private SpecialityRepository specialityRepository ;
    final private ImageService imageService ;

    public DoctorDetails findByUser(User user) {

        return doctorRepository.findByUser(user) ;
    }


    public Speciality findSpecialityById(Long id) {

        Optional<Speciality> specialityOptional =  specialityRepository.findById(id) ;
        specialityOptional.orElseThrow(() -> new
                NotFoundException("Speciality id not found -" + id)) ;

        return specialityOptional.get() ;
    }

    public void save(DoctorDetails doctorDetails) {

        doctorRepository.save(doctorDetails);
    }


    public List<Speciality> findAllSpeciality() {

        return specialityRepository.findAll();
    }

    @Transactional
    public void saveSpeciality(String specialityName , MultipartFile multipartFile) throws IOException {

        String imagePath = specialityName
                + "-Speciality"
                + imageService.getExtensionImage(multipartFile.getOriginalFilename()) ;

        Speciality speciality = Speciality
                .builder()
                .name(specialityName)
                .image(imagePath)
                .build();

        specialityRepository.save(speciality) ;

        if(speciality.getId() == 0L)
            throw new CustomException("Failed to save Speciality");

        imageService.saveImage(multipartFile , imagePath);
    }

    public Speciality findBySpecialityName(String specialityName) {
        Optional<Speciality> speciality = specialityRepository.findByName(specialityName) ;
        return speciality.orElse(null);
    }

    @Transactional
    public void deleteSpecialityById(Long id) {

        specialityRepository.deleteById(id);
    }


}
