package dev.afcasco.fctsorterbackend.controller;

import dev.afcasco.fctsorterbackend.repository.UserRepository;
import dev.afcasco.fctsorterbackend.entity.User;
import dev.afcasco.fctsorterbackend.modelassembler.UserModelAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name="User Management")
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler assembler;

    public UserController(UserRepository userRepository, UserModelAssembler assembler) {
        this.userRepository = userRepository;
        this.assembler = assembler;
    }


    @GetMapping("/{id}")
    public EntityModel<User> findById(@PathVariable Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("No user exists for id: " + id));
        return assembler.toModel(user);
    }

    @GetMapping
    public List<User> findAll(){
        /*User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("No user exists for id: " + id));
        return EntityModel.of(user,)*/
        return userRepository.findAll();
    }
}
