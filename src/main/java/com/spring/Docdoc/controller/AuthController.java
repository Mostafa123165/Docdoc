package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.AuthenticationResponse;
import com.spring.Docdoc.dto.ChangePasswordDto;
import com.spring.Docdoc.dto.LoginDto;
import com.spring.Docdoc.dto.SignUpDto;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.mapper.UserMapper;
import com.spring.Docdoc.service.AuthService;
import com.spring.Docdoc.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    final private AuthService authService ;
    final private UserMapper userMapper ;
    final private UserService userService ;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup(@Valid @RequestBody SignUpDto signUpRequestDto) throws ExecutionException, InterruptedException {

        User user = userMapper.mapToUser(signUpRequestDto);

       authService.signUp(user , signUpRequestDto.getSpecialityId() , signUpRequestDto.getAboutMe());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseMessage.builder()
                        .message("Add user Successfully - " + user.getId())
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginDto loginDto) {

         return ResponseEntity.status(HttpStatus.OK)
                    .body(authService.login(loginDto));
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<ResponseMessage> activateAccount(@RequestParam String otp ,
                                                           @RequestParam String email) {

        authService.activateUserAccount(otp,email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Email activation successful")
                        .build());
    }

    @GetMapping("/checkOtp")
    public ResponseEntity<ResponseMessage> checkOtp(@RequestParam String otp ,
                                                    @Email @RequestParam String email) {

        authService.checkOtp(otp,email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("OTP is correct")
                        .build());
    }


    @PutMapping("/refreshOtp")
    public ResponseEntity<ResponseMessage> refreshOtp(@RequestParam String email) {

        authService.refreshOtp(email);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .message("Sent new otp successfully")
                        .status(HttpStatus.CREATED.value())
                        .build());
    }

    @PutMapping("/forgotPassword")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody ChangePasswordDto passwordDto){
        authService.changePassword(passwordDto) ;

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage.builder()
                        .message("Changed password successfully")
                        .status(HttpStatus.OK.value())
                        .build());
    }



}
