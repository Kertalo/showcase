package com.example.showcase.controllers;

import com.example.showcase.entity.Role;
import com.example.showcase.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://showcase-pd.ru"})
@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role saveRole = roleService.createRole(role);
        return new ResponseEntity<>(saveRole, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Role> getRole(@PathVariable("id")  int roleId) {
        Role role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") int roleId, @RequestBody Role updateRole) {
        Role role = roleService.updateRole(roleId, updateRole);
        return ResponseEntity.ok(role);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") int roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Role is deleted");
    }
}
