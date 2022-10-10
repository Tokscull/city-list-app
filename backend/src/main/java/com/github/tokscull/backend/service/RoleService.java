package com.github.tokscull.backend.service;

import com.github.tokscull.backend.exeption.EntityNotFoundException;
import com.github.tokscull.backend.model.Role;
import com.github.tokscull.backend.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found for name: " + name));
    }
}
