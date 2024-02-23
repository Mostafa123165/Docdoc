package com.spring.Docdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {


    private Long id;
    private String password;
    private String userName;
    private Boolean enabled;
    private Boolean accountNonExpired ;
    private Boolean credentialsNonExpired ;
    private Boolean accountNonLocked ;
    private Collection<? extends GrantedAuthority> authorities ;
    private String email ;
    private Instant createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
