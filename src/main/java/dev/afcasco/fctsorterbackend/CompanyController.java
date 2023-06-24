package dev.afcasco.fctsorterbackend;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/company")
public class CompanyController {


    private final CompanyService service;
    private CsvFilter csvFilter;


    public CompanyController(CompanyService service, CsvFilter csvFilter) {
        this.service = service;
        this.csvFilter = csvFilter;
    }


    @GetMapping("/findAll")
    public List<Company> findAll(){
        /*List<Company> read = csvFilter.readAllFromCsv();
        read.forEach(i->{
            System.out.println(i);
            service.save(i);
        });*/

        return service.findAll();
    }

    @GetMapping("/findByZipCode")
    public List<Company> findByZipCode(@RequestParam("zip") String zip){
        return service.findCompanyByZipCode(zip);
    }

    @GetMapping("/findByZipCodeStartsWith")
    public List<Company> findByZipCodeStartsWith(@RequestParam("zip") String zip){
        return service.findCompaniesByZipCodeStartsWith(zip);
    }

    @GetMapping("/findByZipCodeContains")
    public List<Company> cpStartsWith(@RequestParam("zip") String zip){
        return service.findCompaniesByZipCodeStartsWith(zip);
    }

    @GetMapping("/findByCity")
    public List<Company> findByCity(@RequestParam("city") String city){
        return service.findAllByCityEqualsIgnoreCase(city);
    }

    @GetMapping("/findByNameContains")
    public List<Company> findByNameContains(@RequestParam("text") String text){
        return service.findAllByNameContainsIgnoreCase(text);
    }
}
