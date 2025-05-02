package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProjectDTO {
    private Integer id;
    private Integer trackId;
    private String title;
    private String description;
    private Integer grade;
    private String repo;
    private MultipartFile[] screenshots;
    private String presentation;
    private List<Integer> tagsId;
    private List<Integer> usersId;
    private Integer dateId;
    private MultipartFile mainScreenshot;
}
