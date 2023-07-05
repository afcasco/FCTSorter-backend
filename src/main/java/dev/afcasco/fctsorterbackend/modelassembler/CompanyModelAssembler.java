package dev.afcasco.fctsorterbackend.modelassembler;

import dev.afcasco.fctsorterbackend.controller.CompanyController;
import dev.afcasco.fctsorterbackend.model.Company;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyModelAssembler implements RepresentationModelAssembler<Company, EntityModel<Company>> {
    @Override
    public @NonNull EntityModel<Company> toModel(@NonNull Company entity){
        return EntityModel.of(entity,
                linkTo(methodOn(CompanyController.class).findById(entity.getId())).withSelfRel(),
                linkTo(methodOn(CompanyController.class).findAll()).withRel("companies"));
    }
}