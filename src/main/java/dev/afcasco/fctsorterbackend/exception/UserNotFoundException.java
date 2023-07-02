package dev.afcasco.fctsorterbackend.exception;

import javax.naming.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
