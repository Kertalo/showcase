package com.example.showcase.service.impl;

import com.example.showcase.entity.Role;
import com.example.showcase.repository.RoleRepository;
import com.example.showcase.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Override
    public Iterable<Role> save(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }

}
