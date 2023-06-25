package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.dao.CompanyRepository;
import dev.afcasco.fctsorterbackend.entity.Status;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findCompanyByZipCode(String zipCode) {
        return companyRepository.findCompanyByZipCode(zipCode);
    }

    @Override
    public List<Company> findCompaniesByZipCodeStartsWith(String startsWith) {
        return companyRepository.findCompaniesByZipCodeStartsWith(startsWith);
    }

    @Override
    public List<Company> findCompaniesByZipCodeContains(String contains) {
        return companyRepository.findCompaniesByZipCodeContains(contains);
    }

    @Override
    public List<Company> findAllByCityEqualsIgnoreCase(String city) {
        return companyRepository.findAllByCityEqualsIgnoreCase(city);
    }

    @Override
    public List<Company> findAllByNameContainsIgnoreCase(String text) {
        return companyRepository.findAllByNameContainsIgnoreCase(text);
    }

    @Override
    public List<Company> findAllByStatus(Status status) {
        return companyRepository.findAllByStatus(status);
    }


    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }
}
