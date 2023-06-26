package dev.afcasco.fctsorterbackend.service;

import dev.afcasco.fctsorterbackend.entity.ERole;
import dev.afcasco.fctsorterbackend.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleService roleService;

    public RoleServiceImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Optional<Role> findByName(ERole name) {
        return roleService.findByName(name);
    }
}
