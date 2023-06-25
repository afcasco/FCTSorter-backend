package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class CompanyNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CompanyNofFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFoundHandler(CompanyNofFoundException e){
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidArgumentHandler(MethodArgumentTypeMismatchException e){
        return "Wrong format for parameter " + e.getParameter().getParameterName();
    }
}
