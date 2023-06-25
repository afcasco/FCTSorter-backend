package dev.afcasco.fctsorterbackend;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.Status;
import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.service.CompanyService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;


@RestController
public class CompanyController {


    private final CompanyService service;
    private CsvFilter csvFilter;


    public CompanyController(CompanyService service, CsvFilter csvFilter) {
        this.service = service;
        this.csvFilter = csvFilter;
    }


    @GetMapping("/companies")
    public List<Company> findAll() {
        return service.findAll();
    }

    @SneakyThrows
    @GetMapping("/companies/{id}")
    public Company getCompany(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new CompanyNofFoundException(id));
    }

    @PutMapping("/companies/{id}")
    public Company replaceCompany(@RequestBody Company newCompany, @PathVariable Long id) {
        return service.findById(id)
                .map(company -> {
                    company.setCif(newCompany.getCif());
                    company.setName(newCompany.getName());
                    company.setAddress(newCompany.getAddress());
                    company.setCity(newCompany.getCity());
                    company.setZipCode(newCompany.getZipCode());
                    company.setPhone(newCompany.getPhone());
                    return service.save(company);
                })
                .orElseGet(() -> {
                    newCompany.setId(id);
                    return service.save(newCompany);
                });
    }

    @PostMapping("/companies")
    public Company newCompany(@RequestBody Company company) {
        return service.save(company);
    }

    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("/companies/zip/{zip}")
    public List<Company> findByZipCode(@PathVariable("zip") String zip) {
        return service.findCompanyByZipCode(zip);
    }


    @GetMapping("/companies/city/{city}")
    public List<Company> findByCity(@PathVariable("city") String city) {
        return service.findAllByCityEqualsIgnoreCase(city);
    }

    @GetMapping("/companies/status/{status}")
    public List<Company> findAllByStatus(@PathVariable("status") Status status) {
        return service.findAllByStatus(status);
    }

    @GetMapping("/companies/nameContains")
    public List<Company> findByNameContains(@RequestParam("text") String text) {
        return service.findAllByNameContainsIgnoreCase(text);
    }

    @GetMapping("/companies/zipStartsWith")
    public List<Company> findByZipCodeStartsWith(@RequestParam("zip") String zip) {
        return service.findCompaniesByZipCodeStartsWith(zip);
    }

    @GetMapping("/zipContains")
    public List<Company> cpStartsWith(@RequestParam("zip") String zip) {
        return service.findCompaniesByZipCodeStartsWith(zip);
    }

}
