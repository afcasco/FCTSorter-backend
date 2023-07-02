package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.RoleNotFoundException;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Priority(3)
public class RoleNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundHandler(RoleNotFoundException e){
        return e.getMessage();
    }
}
