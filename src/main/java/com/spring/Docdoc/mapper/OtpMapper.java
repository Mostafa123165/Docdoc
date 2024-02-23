package com.spring.Docdoc.mapper;

import com.spring.Docdoc.entity.Otp;
import com.spring.Docdoc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface OtpMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "otpValue" , source = "otp")
    @Mapping(target = "createdAt" , expression = "java(java.time.Instant.now())")
    @Mapping(target = "expiredAt" , expression = "java(java.time.Instant.now().plusSeconds(60*60))")
    @Mapping(target = "user" , source = "user")
    Otp mapToOtp(User user , String otp) ;


}
