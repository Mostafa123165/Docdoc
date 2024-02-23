package com.spring.Docdoc.mapper;

import com.spring.Docdoc.dto.SignUpDto;
import com.spring.Docdoc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "image" , ignore = true)
    @Mapping(target = "isActivated", expression = "java(Boolean.FALSE)")
    @Mapping(target = "CreatedAt" , expression = "java(java.time.Instant.now())")
    User mapToUser(SignUpDto signUpDto);

}
