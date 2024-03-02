package com.spring.Docdoc.controller;

import com.spring.Docdoc.dto.UserDto;
import com.spring.Docdoc.entity.User;
import com.spring.Docdoc.exception.ResponseMessage;
import com.spring.Docdoc.mapper.UserMapper;
import com.spring.Docdoc.service.AuthService;
import com.spring.Docdoc.service.UserService;

import jakarta.validation.Valid;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;
    final private AuthService authService;
    final private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDto> findUser() {
        User user = authService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK)
                        .body(userMapper.mapToUserDto(user));
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> updateUser(@Valid @RequestBody UserDto userDto) {

        User user = authService.getCurrentUser();
        userService.phoneCheck(userDto.getPhone(), user);
        userService.update(userMapper.mapToUser(user, userDto));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMessage
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Updated user successfully")
                        .build());
    }
}
