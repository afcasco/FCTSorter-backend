package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.AlreadyLoggedInException;
import dev.afcasco.fctsorterbackend.model.ErrorMessage;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.Instant;
import java.util.Date;


@RestControllerAdvice
    @Priority(2)
    public class AlreadyLoggedInAdvice {

        @ResponseBody
        @ExceptionHandler(AlreadyLoggedInException.class)
        @ResponseStatus(HttpStatus.OK)
        public ErrorMessage alreadyLoggedInHandler(AlreadyLoggedInException e){
            return new ErrorMessage(
                    HttpStatus.OK.value(),
                    Date.from(Instant.now()),
                    "Aready logged in",
                    "user was already logged in");
        }
}