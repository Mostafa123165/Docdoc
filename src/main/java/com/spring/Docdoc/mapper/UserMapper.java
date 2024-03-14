package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.SignUpDto;
import com.spring.Docdoc.dto.UserDto;
import com.spring.Docdoc.entity.User;
import jakarta.persistence.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "image" , ignore = true)
    @Mapping(target = "isActivated", expression = "java(Boolean.FALSE)")
    @Mapping(target = "createdAt" , expression = "java(java.time.Instant.now())")
    User mapToUser(SignUpDto signUpDto);

    @Mapping(target = "firstName",source = "userDto.firstName")
    @Mapping(target = "lastName",source = "userDto.lastName")
    @Mapping(target = "phone",source = "userDto.phone")
    @Mapping(target = "image" , source = "user.image")
    @Mapping(target = "email" , source = "user.email")
    @Mapping(target = "password" , source = "user.password")
    @Mapping(target = "role" , source = "user.role")
    @Mapping(target = "id" , source = "user.id")
    User mapToUser(User user,UserDto userDto);

    UserDto mapToUserDto(User user);

}
