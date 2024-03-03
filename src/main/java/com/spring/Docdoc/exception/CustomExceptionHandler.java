package com.spring.Docdoc.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(1)
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ResponseMessage> customException(CustomException exception) {

        ResponseMessage customMessage =  ResponseMessage.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage()).build() ;

        return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(customMessage);
    }

    @ExceptionHandler
    public ResponseEntity<Object> validException(MethodArgumentNotValidException validException) {
        List<ObjectError> objectErrors = validException.getBindingResult().getAllErrors() ;
        LinkedHashMap<String , Object> response = new LinkedHashMap<>() ;

        List<String> errors = objectErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()); ;

        response.put("status" , HttpStatus.BAD_REQUEST.value());
        response.put("Validation errors" , errors) ;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ResponseMessage> notFoundException(NotFoundException notFoundException) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseMessage.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(notFoundException.getMessage()).build());
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ResponseMessage> badCredentialsException() {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseMessage.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Incorrect username or password.").build());
    }


}
