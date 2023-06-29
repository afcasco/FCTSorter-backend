package dev.afcasco.fctsorterbackend.repository;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    List<Company> findCompanyByZipCode(String zipCode);
    List<Company> findCompaniesByZipCodeStartsWith(String startsWith);
    List<Company> findAllByCityEqualsIgnoreCase(String city);
    List<Company> findAllByNameContainsIgnoreCase(String text);
    List<Company> findAllByStatus(Status status);
    boolean existsById(Long id);
}