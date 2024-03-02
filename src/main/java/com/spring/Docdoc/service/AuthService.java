package com.spring.Docdoc.service;

import com.spring.Docdoc.dto.*;
import com.spring.Docdoc.entity.Otp;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.CustomException;
import com.spring.Docdoc.exception.NotFoundException;
import com.spring.Docdoc.mapper.UserCustomUserDetailsMapper;
import com.spring.Docdoc.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    final private UserService userService;
    final private mailService mailService;
    final private JwtProvider jwtProvider;
    final private AuthenticationManager authenticationManager;
    final private BCryptPasswordEncoder bCryptPasswordEncoder ;
    final private UserCustomUserDetailsMapper userCustomUserDetailsMapper ;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    @Transactional
    public void signUp(User user , Long specialityId , String aboutMe) throws ExecutionException, InterruptedException {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        CompletableFuture<Void> emailAndPhoneCheck = emailAndPhoneCheck(user) ;

        // Block and wait until emailAndPhoneCheck finished
        emailAndPhoneCheck.get();

        userService.save(user,specialityId,aboutMe);

        if (user.getId() == null) {
            throw new CustomException("Saved user unsuccessful - " +
                    user.getEmail());
        }

        sendOtp(user, 0L) ;
    }

    private CompletableFuture<Void> emailAndPhoneCheck(User user) {
        CompletableFuture<Void> phoneCheckFuture = CompletableFuture.runAsync(() ->
                checkOnPhone(user.getPhone()));

        CompletableFuture<Void> emailCheckFuture = CompletableFuture.runAsync(() ->
                checkOnEmail(user.getEmail()));

        return  CompletableFuture.allOf(phoneCheckFuture , emailCheckFuture) ;

    }

    private void sendOtp(User user , Long oldOtpId) {
        mailService.sendOtp(NotificationEmailDto
                        .builder()
                        .subject("Docdoc")
                        .recipient(user.getEmail())
                        .build()
                , user
                , oldOtpId
        );
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRED)
    public void checkOnEmail(String email) {

        User userWithEmail = userService.getByEmail(email);
        if (userWithEmail != null)
            throw new CustomException("Email already exists before - " +
                    userWithEmail.getEmail());
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRED)
    public void checkOnPhone(String phone) {

        User userWithPhone = userService.getByPhone(phone);
        if (userWithPhone != null)
            throw new CustomException("phone already exists before - " +
                    userWithPhone.getPhone());
    }



    public AuthenticationResponse login(LoginDto loginDto) {

       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()));

       String token = jwtProvider.generateJwt(authentication);

       return AuthenticationResponse
               .builder()
               .status(HttpStatus.OK.value())
               .token(token)
               .message("Logged in successfully")
               .createAt(jwtProvider.convertFromInstantToString(Instant.now()))
               .expiredAt(jwtProvider.convertFromInstantToString(
                       Instant.now().plusSeconds(jwtExpirationTime)))
               .build();
    }

    @Transactional
    public void activateUserAccount(String otp, String email) {

        User user = activateValidation(otp,email);
        userService.update(user);
    }

    private User activateValidation(String otp, String email) {
        User user = userService.getByEmail(email) ;

        if(user == null)
            throw new NotFoundException("User with email not found: " + email);

        if(user.getIsActivated())
            throw new CustomException("Your email is already activated");

        Otp otpEntity = mailService.getByUser(user);

        if (otpEntity == null)
            throw new CustomException("OTP not found for user");

        if(otpEntity.getExpiredAt().isBefore(Instant.now()))
            throw new CustomException("Otp has Expired - " + otp) ;

        if (!StringUtils.equals(otpEntity.getOtpValue(), otp))
            throw new CustomException("Incorrect OTP: " + otp);

        user.setIsActivated(true);

        return user ;

    }

    public void refreshOtp(String email) {
        User user = userService.getByEmail(email) ;
        Otp otp = mailService.getByUser(user) ;
        sendOtp(user,otp.getId());
    }


    public void changePassword(ChangePasswordDto passwordDto) {
        if(passwordDto.isValid()) {
           User user = userService.getByEmail(passwordDto.getEmail()) ;
           user.setPassword(bCryptPasswordEncoder.encode(passwordDto.getNewPassword()));
           userService.update(user);
        }
        else
            throw new CustomException("Passwords do not match. Please make sure the passwords match.");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return  userCustomUserDetailsMapper.mapToUser(customUserDetails) ;
    }

    public void checkOtp(String otp, String email) {

        User user = userService.getByEmail(email) ;
        if(user == null) throw new NotFoundException("User not found with email: " + email);

        Otp otpEntity = mailService.getByUser(user);

        if (!StringUtils.equals(otpEntity.getOtpValue(), otp))
            throw new CustomException("Incorrect OTP: " + otp);

    }
}
