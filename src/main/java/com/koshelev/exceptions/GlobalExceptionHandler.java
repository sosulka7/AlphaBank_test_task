package com.koshelev.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchInvalidRequestArgument(InvalidRequestArgument e) {
        return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchFeignException(FeignException e){
        String error = e.getMessage().substring(e.getMessage().indexOf("description") + 15, e.getMessage().length() - 4);
        return new ResponseEntity<>(new AppError(e.status(), error), HttpStatus.valueOf(e.status()));
    }
}
