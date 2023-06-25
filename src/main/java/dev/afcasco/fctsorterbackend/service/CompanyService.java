package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.Status;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    List<Company> findAll();

    List<Company> findCompanyByZipCode(String zipCode);

    List<Company> findCompaniesByZipCodeStartsWith(String startsWith);

    List<Company> findCompaniesByZipCodeContains(String contains);

    List<Company> findAllByCityEqualsIgnoreCase(String city);

    List<Company> findAllByNameContainsIgnoreCase(String text);

    List<Company> findAllByStatus(Status status);

    Company save(Company company);

    Optional<Company> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);




}
