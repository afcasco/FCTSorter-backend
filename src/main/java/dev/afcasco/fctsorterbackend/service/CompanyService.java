package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();

    List<Company> findCompanyByZipCode(String zipCode);

    List<Company> findCompaniesByZipCodeStartsWith(String startsWith);

    List<Company> findCompaniesByZipCodeContains(String contains);

    List<Company> findAllByCityEqualsIgnoreCase(String city);

    List<Company> findAllByNameContainsIgnoreCase(String text);

    void save(Company company);


}
