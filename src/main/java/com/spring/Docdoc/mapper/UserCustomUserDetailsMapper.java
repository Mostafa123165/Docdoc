package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.CustomUserDetails;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.utilits.Enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.thymeleaf.util.StringUtils;

import java.util.Collection;

@Mapper(componentModel = "spring" )
public interface UserCustomUserDetailsMapper {

    @Mapping(target = "role" , expression = "java(getRole(customUserDetails.getAuthorities()))")
    @Mapping(target = "isActivated" , expression = "java(Boolean.TRUE)")
    User mapToUser (CustomUserDetails customUserDetails) ;

    default Role getRole(Collection<? extends GrantedAuthority> roles) {
        Role role = null ;
        for(GrantedAuthority authority : roles) {
            if(StringUtils.equals(authority.getAuthority(), "ROLE_USER"))
                role = Role.USER ;
            else
                role = Role.DOCTOR;
        }

        return role ;
    }
}
