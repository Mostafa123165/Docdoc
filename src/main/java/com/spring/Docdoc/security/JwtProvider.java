package com.spring.Docdoc.security;

import com.spring.Docdoc.dto.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class JwtProvider {

    final private JwtEncoder jwtEncoder;
    final private JwtDecoder jwtDecoder;

    public String generateJwt(Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .subject(customUserDetails.getEmail())
                .id(customUserDetails.getId().toString())
                .issuedAt(customUserDetails.getCreatedAt())
                .expiresAt(customUserDetails.getCreatedAt().plusSeconds(60*60*24*30*12))
                .claim("userName" , customUserDetails.getUsername())
                .claim("authorities",customUserDetails.getAuthorities())
                .issuer("self")
                .build();

        return  this.jwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue() ;
    }

    public String convertFromInstantToString(Instant instant) {
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("GMT+2"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return zonedDateTime.format(formatter) ;
    }

    public String validateJwt(String jwt) {
        return this.jwtDecoder
                .decode(jwt)
                .getSubject() ;
    }
}
