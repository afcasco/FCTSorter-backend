package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.model.ERole;
import dev.afcasco.fctsorterbackend.model.RefreshToken;
import dev.afcasco.fctsorterbackend.model.Role;
import dev.afcasco.fctsorterbackend.model.User;
import dev.afcasco.fctsorterbackend.exception.UserNotFoundException;
import dev.afcasco.fctsorterbackend.modelassembler.UserModelAssembler;
import dev.afcasco.fctsorterbackend.payload.request.UserUpdateData;
import dev.afcasco.fctsorterbackend.repository.RoleRepository;
import dev.afcasco.fctsorterbackend.repository.UserRepository;
import dev.afcasco.fctsorterbackend.security.services.RefreshTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "User", description = "User Management Endpoints")
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserModelAssembler assembler;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, UserModelAssembler assembler,
                          PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.assembler = assembler;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }


    @SneakyThrows
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @GetMapping("/{id}")
    public EntityModel<User> findById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return assembler.toModel(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public CollectionModel<EntityModel<User>> findAll() {
        List<EntityModel<User>> users = userRepository.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).findAll()).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateData data, @PathVariable Long id) {
        User updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.setEmail(data.getEmail());
                    user.setPassword(passwordEncoder.encode(data.getPassword()));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SneakyThrows
    @PutMapping("/{id}/addrole/{eRole}")
    public ResponseEntity<?> addRole(@PathVariable Long id, @PathVariable ERole eRole) {

        Role role = roleRepository.findByName(eRole).orElseThrow();

        User updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.addRole(role);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));

        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/derole/{eRole}")
    @SneakyThrows
    public ResponseEntity<?> removeRole(@PathVariable Long id, @PathVariable ERole eRole) {

        Role role = roleRepository.findByName(eRole).orElseThrow();

        User updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.removeRole(role);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));

        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/revoke")
    @SneakyThrows
    public ResponseEntity<?> revokeToken(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        RefreshToken refreshToken = refreshTokenService.findByUser(user);
        if(refreshToken != null){
            refreshTokenService.deleteByUserId(id);
            return ResponseEntity.ok().body("refresh token deleted");
        }
        return ResponseEntity.notFound().build();
    }

}

