package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.UserNotFoundException;
import dev.afcasco.fctsorterbackend.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;

@RestControllerAdvice
public class UserNotFoundAdvice {

    /*
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundHandler(UserNotFoundException e){
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                Date.from(Instant.now()),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
    }*/

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<? extends ErrorMessage> userNotFoundHandler(UserNotFoundException e){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                Date.from(Instant.now()),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
        return ResponseEntity.status(errorMessage.getStatusCode()).body(errorMessage);
    }
}