package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String fullName;
    private Integer roleId;
    private String login;
    private MultipartFile image;
}
