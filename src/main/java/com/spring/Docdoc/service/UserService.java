package com.spring.Docdoc.service;

import com.spring.Docdoc.entity.DoctorDetails;
import com.spring.Docdoc.entity.Speciality;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.repository.UserRepository;
import com.spring.Docdoc.utilits.Enums.Role;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    final private UserRepository userRepository ;
    final private DoctorService doctorService ;


    @Transactional
    public void save(User user , Long specialityId , String aboutMe) {
        userRepository.save(user);

        if(user.getRole() == Role.DOCTOR) {

            if(specialityId == null) throw new CustomException("specialityId is required ") ;

            Speciality speciality = doctorService.findSpecialityById(specialityId) ;

            doctorService.save(DoctorDetails.builder()
                    .user(user)
                    .rate(0.0)
                    .aboutMe(aboutMe)
                    .speciality(speciality)
                    .numOfReviews(0L)
                    .build());
        }
    }

    public void update(User user) {

        userRepository.save(user);
    }

    public User getByEmail(String email) {

       Optional<User> user =  userRepository.findByEmail(email) ;
       return user.orElse(null);
    }

    public User getByPhone(String phone) {
        Optional<User> user =  userRepository.findByPhone(phone) ;
        return user.orElse(null);
    }

    public void phoneCheck(String phone , User user) {
        User userPhone =  getByPhone(phone) ;
        if(userPhone != null &&
                !Objects.equals(userPhone.getId(), user.getId())) {
            throw new CustomException("phone already exists before - " +
                    phone);
        }
    }


    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id) ;
        return user.orElse(null) ;
    }
}
