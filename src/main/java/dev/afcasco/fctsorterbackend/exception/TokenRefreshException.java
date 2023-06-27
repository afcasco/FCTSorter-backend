package dev.afcasco.fctsorterbackend.exception;

public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUUID = 1L;

    public TokenRefreshException(String token, String message){
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
