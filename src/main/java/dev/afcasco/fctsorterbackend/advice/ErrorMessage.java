package dev.afcasco.fctsorterbackend.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorMessage {
    private int statusCode;
    private Date timeStamp;
    private String message;
    private String description;
}
