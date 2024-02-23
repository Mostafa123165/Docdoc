package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.AuthenticationResponse;
import com.spring.Docdoc.dto.ChangePasswordDto;
import com.spring.Docdoc.dto.LoginDto;
import com.spring.Docdoc.dto.SignUpDto;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.MessageResponse;
import com.spring.Docdoc.mapper.UserMapper;
import com.spring.Docdoc.service.AuthService;
import jakarta.mail.Message;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    final private AuthService authService ;
    final private UserMapper userMapper ;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignUpDto signUpRequestDto) throws ExecutionException, InterruptedException {

        User user = userMapper.mapToUser(signUpRequestDto);

       authService.signUp(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                        .message("Add user Successfully - " + user.getId())
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDto loginDto) {

         return ResponseEntity.status(HttpStatus.OK)
                    .body(authService.login(loginDto));
    }

    @GetMapping("/activateAccount")
    public ResponseEntity<MessageResponse> activateAccount(@RequestParam String otp ,
                                                           @RequestParam String email) {

        authService.activateUserAccount(otp,email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Email activation successful")
                        .build());
    }

    @PutMapping("/refreshOtp")
    public ResponseEntity<MessageResponse> refreshOtp(@RequestParam String email) {

        authService.refreshOtp(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .message("Sent new otp successfully")
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @PutMapping("/forgotPassword")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody ChangePasswordDto passwordDto){
        authService.changePassword(passwordDto) ;

        return ResponseEntity.status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .message("Changed password successfully")
                        .status(HttpStatus.OK.value())
                        .build());
    }



}
