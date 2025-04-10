package com.example.showcase.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "create_track", nullable = false)
    private Boolean createTrack = false;

    @Column(name = "read_track", nullable = false)
    private Boolean readTrack = false;

    @Column(name = "update_track", nullable = false)
    private Boolean updateTrack = false;

    @Column(name = "delete_track", nullable = false)
    private Boolean deleteTrack = false;

    @Column(name = "create_project", nullable = false)
    private Boolean createProject = false;

    @Column(name = "read_project", nullable = false)
    private Boolean readProject = false;

    @Column(name = "update_project", nullable = false)
    private Boolean updateProject = false;

    @Column(name = "delete_project", nullable = false)
    private Boolean deleteProject = false;

    @Column(name = "create_user", nullable = false)
    private Boolean createUser = false;

    @Column(name = "read_user", nullable = false)
    private Boolean readUser = false;

    @Column(name = "update_user", nullable = false)
    private Boolean updateUser = false;

    @Column(name = "delete_user", nullable = false)
    private Boolean deleteUser = false;

    @Column(name = "create_tag", nullable = false)
    private Boolean createTag = false;

    @Column(name = "read_tag", nullable = false)
    private Boolean readTag = false;

    @Column(name = "update_tag", nullable = false)
    private Boolean updateTag = false;

    @Column(name = "delete_tag", nullable = false)
    private Boolean deleteTag = false;

}