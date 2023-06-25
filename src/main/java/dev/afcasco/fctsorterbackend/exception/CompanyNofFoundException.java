package dev.afcasco.fctsorterbackend.exception;

public class CompanyNofFoundException extends Exception {

    public CompanyNofFoundException(Long id) {
        super("Company with id : " + id + " not found.");
    }
}
