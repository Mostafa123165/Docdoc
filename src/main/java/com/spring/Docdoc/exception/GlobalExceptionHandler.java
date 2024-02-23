package com.spring.Docdoc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MessageResponse> globalException(Exception exception) {

        MessageResponse customMessage =  MessageResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage()).build() ;

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(customMessage);
    }

}
