package dev.afcasco.fctsorterbackend.advice;

import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.model.ErrorMessage;
import jakarta.annotation.Priority;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestControllerAdvice
@Priority(2)
public class CompanyNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler({CompanyNofFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage companyNotFoundHandler(CompanyNofFoundException e){
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                Date.from(Instant.now()),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
    }
}