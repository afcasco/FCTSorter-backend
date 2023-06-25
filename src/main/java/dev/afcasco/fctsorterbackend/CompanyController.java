package dev.afcasco.fctsorterbackend;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.Status;
import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.service.CompanyService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;


@RestController
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }


    @GetMapping("/companies")
    public ResponseEntity<?> findAll() {
        List<Company> companies = service.findAll();
        return new ResponseEntity<>(companies,HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        Company company =  service.findById(id).orElseThrow(() -> new CompanyNofFoundException(id));
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> replaceCompany(@RequestBody Company newCompany, @PathVariable Long id) {
        Company saved = service.findById(id)
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
        return new ResponseEntity<>(saved,HttpStatus.OK);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> newCompany(@RequestBody Company company) {
        return new ResponseEntity<>(service.save(company),HttpStatus.CREATED);
    }

    @DeleteMapping("/companies/{id}")
    public HttpStatus deleteCompany(@PathVariable Long id) {
        if(service.existsById(id)){
            service.deleteById(id);
            return HttpStatus.NO_CONTENT;
        }
        return HttpStatus.NOT_FOUND;
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
