package dev.afcasco.fctsorterbackend.exception;

public class AlreadyLoggedInException extends Throwable {
    public AlreadyLoggedInException(String username) {
        super("User " + username + " was already logged in.");
    }
}
