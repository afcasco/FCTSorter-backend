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
import io.swagger.v3.oas.annotations.Operation;
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



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @Operation(summary = "Get a user by id", description = "Returns a user matching the passed id")
    @SneakyThrows
    public EntityModel<User> findById(@PathVariable Long id)  {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return assembler.toModel(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all users", description = "Returns a list of all the users")
    public CollectionModel<EntityModel<User>> findAll() {
        List<EntityModel<User>> users = userRepository.findAll().stream().map(assembler::toModel).toList();
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).findAll()).withSelfRel());
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @Operation(summary = "Update an existing user", description = "Can be called by a user with role 'ADMIN' or by " +
            "the current logged in user on itself, only modifies email & password")
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

    @PutMapping("/{id}/addrole/{eRole}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a role to user", description = "Adds a role to an existing user")
    public ResponseEntity<?> addRole(@PathVariable Long id, @PathVariable ERole eRole) throws UserNotFoundException {

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

    @PutMapping("/{id}/derole/{eRole}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove a role from user", description = "Removes a role from an existing user")
    public ResponseEntity<?> removeRole(@PathVariable Long id, @PathVariable ERole eRole) throws UserNotFoundException {

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


    @DeleteMapping("/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Invalidate refreshtoken", description = "Invalidated refreshtoken for user with passed userid")
    public ResponseEntity<?> revokeToken(@PathVariable Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        RefreshToken refreshToken = refreshTokenService.findByUser(user);
        if(refreshToken != null){
            refreshTokenService.deleteByUserId(id);
            return ResponseEntity.ok().body("refresh token deleted");
        }
        return ResponseEntity.notFound().build();
    }
}