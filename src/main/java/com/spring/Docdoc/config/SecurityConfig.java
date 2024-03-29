package com.spring.Docdoc.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.spring.Docdoc.exception.CustomAuthenticationEntryPoint;
import com.spring.Docdoc.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    @Autowired
    private JwtAuthenticationFilter JwtAuthorization ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{


        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**",
                                        "/swagger-resources/*",
                                        "/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/index.html")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/image/**")
                                .permitAll()
                                .requestMatchers(Arrays.toString(new HttpMethod[]{HttpMethod.POST, HttpMethod.DELETE}),"/api/clinic")
                                .hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/speciality")
                                .permitAll()
                                .requestMatchers("/api/my-appointment/patient")
                                .hasRole("USER")
                                .requestMatchers("/api/my-appointment/doctor")
                                .hasRole("DOCTOR")
                                .anyRequest()
                                .hasAnyRole("USER","DOCTOR")
                ).exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        httpSecurity.addFilterBefore(JwtAuthorization , UsernamePasswordAuthenticationFilter.class) ;

        return httpSecurity.build() ;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder () {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager() ;
    }

    @Bean
    JwtDecoder jwtDecoder() {

        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {

        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
