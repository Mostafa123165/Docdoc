package com.spring.Docdoc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseMessage> globalException(Exception exception) {

        ResponseMessage customMessage =  ResponseMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage()).build() ;

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(customMessage);
    }

}
