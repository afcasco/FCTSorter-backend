package dev.afcasco.fctsorterbackend.payload.request;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Hidden
@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}