package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.RoleNotFoundException;
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
@Priority(3)
public class RoleNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage roleNotFoundHandler(RoleNotFoundException e){
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                Date.from(Instant.now()),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
    }
}