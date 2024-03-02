package com.spring.Docdoc.service;


import com.spring.Docdoc.dto.CustomUserDetails;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.repository.UserRepository;
import com.spring.Docdoc.utilits.Enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<User> userOptional = userRepository.findByEmail(username) ;
        userOptional.orElseThrow(() ->  new BadCredentialsException("Incorrect username or password.")) ;
        User user = userOptional.get() ;

        return new CustomUserDetails(
                user.getId() ,
                user.getPassword(),
                user.getFirstName() ,
                user.getLastName() ,
                user.getIsActivated() ,
                true,
                true,
                true,
                getAuthorities(user.getRole()),
                user.getEmail(),
                user.getCreatedAt(),
                user.getImage() ,
                user.getPhone()
        );

    }

    Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }
}
