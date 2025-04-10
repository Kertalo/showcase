package com.example.showcase.config;

import com.example.showcase.entity.Permission;
import com.example.showcase.entity.Role;
import com.example.showcase.entity.RolePermission;
import com.example.showcase.repository.PermissionRepository;
import com.example.showcase.repository.RolePermissionRepository;
import com.example.showcase.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (permissionRepository.count() == 0) {
            // Участник
            Permission participantPermission = new Permission();
            participantPermission.setCreateProject(true);
            participantPermission.setReadProject(true);
            participantPermission.setUpdateProject(true);
            participantPermission.setDeleteProject(false);
            participantPermission.setCreateTrack(false);
            participantPermission.setReadTrack(false);
            participantPermission.setUpdateTrack(false);
            participantPermission.setDeleteTrack(false);
            participantPermission.setCreateUser(false);
            participantPermission.setReadUser(false);
            participantPermission.setUpdateUser(false);
            participantPermission.setDeleteUser(false);
            participantPermission.setCreateTag(false);
            participantPermission.setReadTag(false);
            participantPermission.setUpdateTag(false);
            participantPermission.setDeleteTag(false);

            // Администратор
            Permission adminPermission = new Permission();
            adminPermission.setCreateProject(true);
            adminPermission.setReadProject(true);
            adminPermission.setUpdateProject(true);
            adminPermission.setDeleteProject(true);
            adminPermission.setCreateTrack(true);
            adminPermission.setReadTrack(true);
            adminPermission.setUpdateTrack(true);
            adminPermission.setDeleteTrack(true);
            adminPermission.setCreateUser(true);
            adminPermission.setReadUser(true);
            adminPermission.setUpdateUser(true);
            adminPermission.setDeleteUser(true);
            adminPermission.setCreateTag(true);
            adminPermission.setReadTag(true);
            adminPermission.setUpdateTag(true);
            adminPermission.setDeleteTag(true);

            permissionRepository.save(participantPermission);
            permissionRepository.save(adminPermission);
        }

        if (rolePermissionRepository.count() == 0) {
            Role participantRole = roleRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Role 'Participant' not found"));
            Role adminRole = roleRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Role 'Admin' not found"));

            Permission participantPermission = permissionRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Permission for Participant not found"));
            Permission adminPermission = permissionRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Permission for Admin not found"));

            RolePermission participantRolePermission = new RolePermission();
            participantRolePermission.setRole(participantRole);
            participantRolePermission.setPermission(participantPermission);
            rolePermissionRepository.save(participantRolePermission);

            RolePermission adminRolePermission = new RolePermission();
            adminRolePermission.setRole(adminRole);
            adminRolePermission.setPermission(adminPermission);
            rolePermissionRepository.save(adminRolePermission);
        }
    }
}