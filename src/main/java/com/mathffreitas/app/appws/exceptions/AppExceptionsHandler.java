package com.mathffreitas.app.appws.exceptions;

import com.mathffreitas.app.appws.model.response.error.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler( value = {UserServiceException.class} )
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {

        ErrorMessage errorMessageTemplate = new ErrorMessage(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorMessageTemplate, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    // NullPointer, Formatter, ...
    @ExceptionHandler( value = {Exception.class} )
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) {

        ErrorMessage errorMessageTemplate = new ErrorMessage(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorMessageTemplate, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
