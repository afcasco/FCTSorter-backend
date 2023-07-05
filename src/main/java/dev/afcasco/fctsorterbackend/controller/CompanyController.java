package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.model.Company;
import dev.afcasco.fctsorterbackend.modelassembler.CompanyModelAssembler;
import dev.afcasco.fctsorterbackend.model.Status;
import dev.afcasco.fctsorterbackend.exception.CompanyNofFoundException;
import dev.afcasco.fctsorterbackend.repository.CompanyRepository;
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
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/companies")
@PreAuthorize("hasAnyRole('USER','MOD','ADMIN')")
@Tag(name = "Company", description = "Company Management Endpoints")
public class CompanyController {


    private final CompanyRepository repository;
    private final CompanyModelAssembler assembler;


    public CompanyController(CompanyRepository repository, CompanyModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    @GetMapping
    @Operation(summary = "List all companies", description = "Returns a list of all the companies in the database")
    public CollectionModel<EntityModel<Company>> findAll() {
        List<EntityModel<Company>> companies = repository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAll()).withSelfRel());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a company by id", description = "Returns a company matching the passed id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok - Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The company was not found"),
            @ApiResponse(responseCode = "400", description = "Bad request - Wrong format for parameter id")
    })
    @SneakyThrows
    public EntityModel<Company> findById(@PathVariable
                                         @Parameter(name = "id", description = "Company id", example = "1") Long id) {
        return repository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new CompanyNofFoundException(id));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @Operation(summary = "Update an existing company", description = "Updates an existing company given it's id, or creates a new one if it does not exist")
    @Parameter(name = "id", description = "Id of the company to update", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Resource update/created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")
    })
    public ResponseEntity<?> replaceCompany(@Valid @RequestBody Company newCompany, @PathVariable Long id) {
        Company updatedCompany = repository.findById(id)
                .map(company -> {
                    company.setCif(newCompany.getCif());
                    company.setName(newCompany.getName());
                    company.setAddress(newCompany.getAddress());
                    company.setCity(newCompany.getCity());
                    company.setZipCode(newCompany.getZipCode());
                    company.setPhone(newCompany.getPhone());
                    company.setStatus(newCompany.getStatus());
                    return repository.save(company);
                })
                .orElseGet(() -> {
                    newCompany.setId(id);
                    return repository.save(newCompany);
                });

        EntityModel<Company> entityModel = assembler.toModel(updatedCompany);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MOD')")
    @Operation(summary = "Delete a company", description = "Delete an existing company")
    @Parameter(name = "id", description = "Id of the company to delete", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content - Returned when the company matching the id is deleted, or if it does not exist"),
            @ApiResponse(responseCode = "400", description = "Bad request - Wrong format for parameter id")
    })
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    @Operation(summary = "Create a company", description = "Adds a new company to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created - Resource created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")
    })
    public ResponseEntity<?> newCompany(@Valid @RequestBody Company company) {
        EntityModel<Company> entityModel = assembler.toModel(repository.save(company));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @GetMapping("/zip/{zip}")
    @Operation(summary = "Find by zip code, enter either the starting numbers or the full zip code)", description = "Finds all companies in the given zip code")
    @Parameter(name = "zip", description = "Zip code to match", example = "80085")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok - Accepted")})
    public CollectionModel<EntityModel<Company>> findByZipCode(@PathVariable("zip") String zip) {
        List<EntityModel<Company>> companies = repository.findCompaniesByZipCodeStartsWith(zip).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findByZipCode(zip)).withSelfRel());
    }


    @GetMapping("/city/{city}")
    @Operation(summary = "Find by city", description = "Finds all companies in the given city")
    @Parameter(name = "City", description = "City to match", example = "Springfield")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok - Accepted")})
    public CollectionModel<EntityModel<Company>> findAllByCityEqualsIgnoreCase(@PathVariable("city") String city) {
        List<EntityModel<Company>> companies = repository.findAllByCityEqualsIgnoreCase(city).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByCityEqualsIgnoreCase(city)).withSelfRel());
    }


    @GetMapping("/status/{status}")
    @Operation(summary = "Find by status", description = "Finds companies with a given status")
    @Parameter(name = "Status", description = "Status to match (ACTIVE, INACTIVE, MARKED_FOR_REVIEW)", example = "ACTIVE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad request - Error validating data")
    })
    public CollectionModel<EntityModel<Company>> findAllByStatus(@PathVariable("status") Status status) {
        List<EntityModel<Company>> companies = repository.findAllByStatus(status).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByStatus(status)).withSelfRel());
    }


    @GetMapping("/nameContains")
    @Operation(summary = "Find by name containing", description = "Finds companies whose name contains the passed parameter")
    @Parameter(name = "text", description = "text to search for in the companies names", example = "web")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok - Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad request - When passing no parameter")
    })
    public CollectionModel<EntityModel<Company>> findAllByNameContainsIgnoreCase(@RequestParam("text") String text) {
        List<EntityModel<Company>> companies = repository.findAllByNameContainsIgnoreCase(text).stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(companies, linkTo(methodOn(CompanyController.class).findAllByNameContainsIgnoreCase(text)).withSelfRel());
    }

}