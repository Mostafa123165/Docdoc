package com.spring.Docdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class AuthenticationResponse {

    private int status ;
    private String message ;
    private String token ;
    private String createAt ;
    private String expiredAt ;
}
