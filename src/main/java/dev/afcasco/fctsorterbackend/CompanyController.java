package dev.afcasco.fctsorterbackend;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.CompanyModelAssembler;
import dev.afcasco.fctsorterbackend.entity.Status;
import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Tag(name="Companies API")
public class CompanyController {


    private final CompanyService service;
    private final CompanyModelAssembler assembler;


    public CompanyController(CompanyService service, CompanyModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary= "List all companies",description = "Returns a list of all the companies in the database")
    @GetMapping("/companies")
    public CollectionModel<EntityModel<Company>> findAll() {
        List<EntityModel<Company>> companies = service.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAll()).withSelfRel());
    }


    @Operation(summary = "Get a company by id",description = "Returns a company matching the passed id")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The company was not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Wrong format for parameter id")

    })
    @SneakyThrows
    @GetMapping("/companies/{id}")
    public EntityModel<Company> findById(@PathVariable @Parameter(name="id",description = "Company id", example = "1") Long id) {
        Company company = service.findById(id).orElseThrow(() -> new CompanyNofFoundException(id));
        return assembler.toModel(company);
    }


    @PutMapping("/companies/{id}")
    public ResponseEntity<?> replaceCompany(@RequestBody Company newCompany, @PathVariable Long id) {
        Company updatedCompany = service.findById(id)
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

        EntityModel<Company> entityModel = assembler.toModel(updatedCompany);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/companies")
    public ResponseEntity<?> newCompany(@RequestBody Company company) {
        EntityModel<Company> entityModel = assembler.toModel(service.save(company));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @GetMapping("/companies/zip/{zip}")
    public CollectionModel<EntityModel<Company>> findByZipCode(@PathVariable("zip") String zip) {
        List<EntityModel<Company>> companies = service.findCompanyByZipCode(zip).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findByZipCode(zip)).withSelfRel());
    }


    @GetMapping("/companies/city/{city}")
    public CollectionModel<EntityModel<Company>> findAllByCityEqualsIgnoreCase(@PathVariable("city") String city) {
        List<EntityModel<Company>> companies = service.findAllByCityEqualsIgnoreCase(city).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByCityEqualsIgnoreCase(city)).withSelfRel());
    }

    @GetMapping("/companies/status/{status}")
    public CollectionModel<EntityModel<Company>> findAllByStatus(@PathVariable("status") Status status) {
        List<EntityModel<Company>> companies = service.findAllByStatus(status).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByStatus(status)).withSelfRel());
    }

    @GetMapping("/companies/nameContains")
    public CollectionModel<EntityModel<Company>> findAllByNameContainsIgnoreCase(@RequestParam("text") String text) {
        List<EntityModel<Company>> companies = service.findAllByNameContainsIgnoreCase(text).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByNameContainsIgnoreCase(text)).withSelfRel());
    }

    @GetMapping("/companies/zipStartsWith")
    public CollectionModel<EntityModel<Company>> findCompaniesByZipCodeStartsWith(@RequestParam("zip") String zip) {
        List<EntityModel<Company>> companies = service.findCompaniesByZipCodeStartsWith(zip).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findCompaniesByZipCodeStartsWith(zip)).withSelfRel());

    }
}
