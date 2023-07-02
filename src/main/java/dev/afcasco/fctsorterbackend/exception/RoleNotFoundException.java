package dev.afcasco.fctsorterbackend.exception;

public class RoleNotFoundException extends Exception{


    public RoleNotFoundException(String role) {
        super("Role " + role +" does not exist!");
    }
}
