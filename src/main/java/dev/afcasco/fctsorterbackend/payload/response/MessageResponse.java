package dev.afcasco.fctsorterbackend.payload.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Hidden
@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String message;
}