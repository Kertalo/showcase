package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectDTO {
    private Integer trackId;
    private String title;
    private String description;
    private Integer grade;
    private String repo;
    private String[] screenshots;
    private String presentation;
    private List<Integer> tagsId;
    private List<Integer> usersId;
    private String date;
}
