package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String fullName;
    private Integer roleId;
    private String login;
}
