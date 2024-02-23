package com.spring.Docdoc.service;

import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    final private UserRepository userRepository ;

    @Transactional
    public void save(User user) {
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

}
