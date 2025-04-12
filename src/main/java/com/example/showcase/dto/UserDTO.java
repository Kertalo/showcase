package com.example.showcase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

//TODO
public class UserDTO {
    private Integer id; // автогенерация или инкремент
    private String fullName; // ФИО
    private Integer roleId; // Роль Участник - 1
    private String login; // ---
    private String email; // ---
    private MultipartFile image; // ---
    //Курс Общ.
    //Группа Общ.
}
