package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.entity.Company;
import dev.afcasco.fctsorterbackend.entity.CompanyModelAssembler;
import dev.afcasco.fctsorterbackend.entity.Status;
import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.service.CompanyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// TODO add authorization level to methods @PreAuthorize

@RestController
@Tag(name="Companies API")
@RequestMapping("/api")
public class CompanyController {


    private final CompanyServiceImpl service;
    private final CompanyModelAssembler assembler;


    public CompanyController(CompanyServiceImpl service, CompanyModelAssembler assembler) {
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


    @PreAuthorize("hasRole('ADMIN')")
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


    @Operation(summary = "Update an existing company",description = "Updates an existing company given it's id, or creates a new one if it does not exist")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Created - Resource update/created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")

    })
    @PutMapping("/companies/{id}")
    @Parameter(name = "id", description = "Id of the company to update", example = "1")
    public ResponseEntity<?> replaceCompany(@Valid @RequestBody Company newCompany, @PathVariable Long id) {
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

    @SneakyThrows
    @Operation(summary = "Delete a company",description = "Delete an existing company")
    @Parameter(name = "id", description = "Id of the company to delete", example = "1")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204", description = "No content - Returned when the company matching the id is deleted, or if it does not exist"),
            @ApiResponse(responseCode = "400", description = "Bad request - Wrong format for parameter id")

    })
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Create a company",description = "Adds a new company to the database")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Created - Resource created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")

    })
    @PostMapping("/companies")
    public ResponseEntity<?> newCompany(@Valid @RequestBody Company company) {
        EntityModel<Company> entityModel = assembler.toModel(service.save(company));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    // TODO: 25/6/23 add zip number format validation when parameter is changed from String to int in the entity
    @Operation(summary = "Find by zip code",description = "Finds all companies in the given zip code")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),

    })
    @Parameter(name = "zip", description = "Zip code to match", example = "80085")
    @GetMapping("/companies/zip/{zip}")
    public CollectionModel<EntityModel<Company>> findByZipCode(@PathVariable("zip") String zip) {
        List<EntityModel<Company>> companies = service.findCompanyByZipCode(zip).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findByZipCode(zip)).withSelfRel());
    }


    @Operation(summary = "Find by city",description = "Finds all companies in the given city")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),

    })
    @Parameter(name = "City", description = "City to match", example = "Springfield")
    @GetMapping("/companies/city/{city}")
    public CollectionModel<EntityModel<Company>> findAllByCityEqualsIgnoreCase(@PathVariable("city") String city) {
        List<EntityModel<Company>> companies = service.findAllByCityEqualsIgnoreCase(city).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByCityEqualsIgnoreCase(city)).withSelfRel());
    }


    @Operation(summary = "Find by status",description = "Finds companies with a given status")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")

    })
    @Parameter(name = "Status", description = "Status to match (ACTIVE, INACTIVE, MARKED_FOR_REVIEW)", example = "ACTIVE")
    @GetMapping("/companies/status/{status}")
    public CollectionModel<EntityModel<Company>> findAllByStatus(@PathVariable("status") Status status) {
        List<EntityModel<Company>> companies = service.findAllByStatus(status).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByStatus(status)).withSelfRel());
    }

    @Operation(summary = "Find by name containing",description = "Finds companies whose name contains the passed parameter")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad request - When passing no parameter")

    })
    @Parameter(name = "text", description = "text to search for in the companies names", example = "web")
    @GetMapping("/companies/nameContains")
    public CollectionModel<EntityModel<Company>> findAllByNameContainsIgnoreCase(@RequestParam("text") String text) {
        List<EntityModel<Company>> companies = service.findAllByNameContainsIgnoreCase(text).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByNameContainsIgnoreCase(text)).withSelfRel());
    }

    @Operation(summary = "Find by first digits of zip code",description = "Finds companies whose zip code starts with the given digits, useful to get companies by region")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),
    })
    @Parameter(name = "digits", description = "The starting digits of the zip code we want to search for", example = "08 - would give companies from Barcelona region")
    @GetMapping("/companies/zipStartsWith")
    public CollectionModel<EntityModel<Company>> findCompaniesByZipCodeStartsWith(@RequestParam("zip") String zip) {
        List<EntityModel<Company>> companies = service.findCompaniesByZipCodeStartsWith(zip).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findCompaniesByZipCodeStartsWith(zip)).withSelfRel());
    }
}
