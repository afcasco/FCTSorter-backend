package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.dao.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void save(Company company) {
        companyRepository.save(company);
    }
}
