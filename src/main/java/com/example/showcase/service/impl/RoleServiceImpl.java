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
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role updateRole(int roleId, Role updateRole) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(updateRole.getName());
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(int roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }

    @Override
    public Iterable<Role> save(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }

}
