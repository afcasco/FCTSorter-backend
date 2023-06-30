package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.payload.request.UserUpdateData;
import dev.afcasco.fctsorterbackend.repository.UserRepository;
import dev.afcasco.fctsorterbackend.entity.User;
import dev.afcasco.fctsorterbackend.modelassembler.UserModelAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name="User", description = "User Management Endpoints")
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler assembler;

    public UserController(UserRepository userRepository, UserModelAssembler assembler) {
        this.userRepository = userRepository;
        this.assembler = assembler;
    }


    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @GetMapping("/{id}")
    public EntityModel<User> findById(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("No user exists for id: " + id));
        return assembler.toModel(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> findAll(){
        /*User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("No user exists for id: " + id));
        return EntityModel.of(user,)*/
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @PutMapping("{id}")
    public void updateUser(@RequestBody UserUpdateData data, @PathVariable Long id){
        System.out.println(data.getPassword());
        System.out.println(data.getEmail());
    }

}
