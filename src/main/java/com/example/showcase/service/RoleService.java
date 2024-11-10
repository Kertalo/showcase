package com.example.showcase.service;

import com.example.showcase.entity.Role;

import java.util.List;

public interface RoleService {
    Iterable<Role> save(List<Role> roles);
}
