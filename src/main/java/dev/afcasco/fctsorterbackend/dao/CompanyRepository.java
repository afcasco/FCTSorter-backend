package dev.afcasco.fctsorterbackend.dao;

import dev.afcasco.fctsorterbackend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CompanyRepository extends JpaRepository<Company,Long> {

    List<Company> findCompanyByZipCode(String zipCode);

    List<Company> findCompaniesByZipCodeStartsWith(String startsWith);

    List<Company> findCompaniesByZipCodeContains(String contains);
}
