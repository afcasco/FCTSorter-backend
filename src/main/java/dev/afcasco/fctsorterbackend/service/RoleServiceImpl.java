package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.dao.RoleRepository;
import dev.afcasco.fctsorterbackend.entity.ERole;
import dev.afcasco.fctsorterbackend.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }
}
