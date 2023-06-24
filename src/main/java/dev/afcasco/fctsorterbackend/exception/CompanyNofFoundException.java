package dev.afcasco.fctsorterbackend.exception;

public class CompanyNofFoundException extends Exception {

    public CompanyNofFoundException(Long id) {
        super("Could not find company " + id);
    }
}
