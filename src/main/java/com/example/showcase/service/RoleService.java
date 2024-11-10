package com.example.showcase.service;

import com.example.showcase.entity.Role;


import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    Role getRoleById(int roleId);

    List<Role> getAllRoles();

    Role updateRole(int roleId, Role updateRole);

    void deleteRole(int roleId);

    Iterable<Role> save(List<Role> roles);
}
