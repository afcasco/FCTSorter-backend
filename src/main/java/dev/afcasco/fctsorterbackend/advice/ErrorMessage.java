package dev.afcasco.fctsorterbackend.advice;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Date;

@AllArgsConstructor
@Getter
@Hidden
public class ErrorMessage {
    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;
}