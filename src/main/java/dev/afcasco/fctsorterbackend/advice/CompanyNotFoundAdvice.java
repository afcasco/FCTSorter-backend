package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import jakarta.annotation.Priority;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
@Priority(2)
public class CompanyNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CompanyNofFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFoundHandler(CompanyNofFoundException e){
        return e.getMessage();
    }


}
