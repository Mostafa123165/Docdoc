package com.spring.Docdoc.service;


import com.spring.Docdoc.dto.CustomUserDetails;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.repository.UserRepository;
import com.spring.Docdoc.utilits.Enums.Role;
import lombok.AllArgsConstructor;
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

        Optional<User> user = userRepository.findByEmail(username) ;
        user.orElseThrow(() ->  new NotFoundException("Not found user with email - " + username) ) ;

        return new CustomUserDetails(
                user.get().getId() ,
                user.get().getPassword(),
                user.get().getFirstName() + " " + user.get().getLastName() ,
                user.get().getIsActivated() ,
                true,
                true,
                true,
                getAuthorities(user.get().getRole()),
                user.get().getEmail(),
                user.get().getCreatedAt()
        );

    }

    Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
