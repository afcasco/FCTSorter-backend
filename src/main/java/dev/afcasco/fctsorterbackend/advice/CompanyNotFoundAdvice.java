package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CompanyNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CompanyNofFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFoundHandler(CompanyNofFoundException e){
        return e.getMessage();
    }
}
